package com.example.quicknotes.Activities
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quicknotes.Activities.ui.theme.QuickNotesTheme
import com.example.quicknotes.Model.Note
import com.example.quicknotes.Navigation.MyBottomBar
import com.example.quicknotes.Navigation.MyFavButton
import com.example.quicknotes.Navigation.MyTopBar
import com.example.quicknotes.Navigation.NavItem
import com.example.quicknotes.Navigation.Screen
import com.example.quicknotes.R
import com.example.quicknotes.Screens.FavouriteScreen
import com.example.quicknotes.Screens.SplashScreen
import com.example.quicknotes.ViewModel.NoteViewModel

class HomeActivity : ComponentActivity() {

    // Lazy initialization for ViewModel
    val vm: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            QuickNotesTheme {

                // Initialize navController
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Splash.route) {

                    // Splash Screen
                    composable(Screen.Splash.route) {
                        SplashScreen {
                            navController.navigate(Screen.Main.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true } // Remove Splash from back stack
                            }
                        }
                    }

                    // Main Screen
                    composable(Screen.Main.route) {
                        MainScreenContent(vm, navController) // Pass navController to MainScreenContent
                    }

                    // Create Note Screen
                    composable(Screen.CreateNote.route) {
                        // Replace with Compose Screen for Creating Notes (use a composable instead of Activity)
                        CreateNoteActivity()
                    }


                    // Edit Note Screen
                    composable(
                        Screen.ViewEditNote.route,
                        arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        ViewEditNoteActivity()
                    }

              }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        // Check for any changes or updates that might have been made in CreateNoteActivity
        vm.loadNotes()// Ensure the ViewModel is up-to-date
    }
}



@Composable
fun MainScreenContent(vm: NoteViewModel, navController: NavController) {
    val selectedScreen = remember { mutableStateOf<NavItem>(NavItem.Home) }

    Scaffold(
        topBar = {
            MyTopBar()
        },
        bottomBar = {
            MyBottomBar(selectedScreen)
        },

        floatingActionButton = {
            if (selectedScreen.value == NavItem.Home) { // Show FAB only on the Home screen
                MyFavButton(vm)
            }
        },
        floatingActionButtonPosition = FabPosition.End


    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
        ) {
            when (selectedScreen.value) {
                NavItem.Home -> HomeScreen(vm)
                NavItem.Favourite -> FavouriteScreen(vm) // Pass formatted history
            }
        }
    }
}

@Composable
fun HomeScreen(vm: NoteViewModel) {
    val context = LocalContext.current
    val notes by vm.notes.observeAsState(emptyList()) // Using LiveData with observeAsState

  //  val noteList = vm.stateList // Automatically observes changes
    Log.d("HomeScreen", "Current note count: ${notes.size}")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (notes.isEmpty()) {
            item {
                // Display message when the list is empty
                Box(
                    modifier = Modifier
                        .fillParentMaxSize() // Use fillParentMaxSize for LazyColumn
                        .background(Color.Black), // Ensure a contrasting background
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.noNotes),
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {

            items(notes) { note ->
                NoteCard(
                    note = note,
                    onCardClick = {
                        // Print the note ID to log
                        Log.d("NoteCard", "Note ID: ${note.id}")

                        val intent = Intent(context, ViewEditNoteActivity::class.java)
                        intent.putExtra("noteId", note.id) // Pass the note ID
                        context.startActivity(intent)
                                  },
                    onDelete = {
                        // Delete the note from the ViewModel or database
                        vm.deleteNote(note)
                        Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun NoteCard(note: Note, onCardClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = onCardClick), // Make the entire card clickable
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Card elevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Delete button aligned to the right
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Note title
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,

                )

                // Space between title and content
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp) // Adjust size as needed
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Note")
                }
            }

            // Note content with truncation and ellipsis
            Text(
                text = note.note,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray, // Color for note content text
                maxLines = 3, // Limit the content to 3 lines
                overflow = TextOverflow.Ellipsis, // Truncate and add ellipsis if text overflows
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}