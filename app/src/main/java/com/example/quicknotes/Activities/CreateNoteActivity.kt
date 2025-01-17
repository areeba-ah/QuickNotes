package com.example.quicknotes.Activities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.quicknotes.Activities.ui.theme.QuickNotesTheme
import com.example.quicknotes.Model.Note
import com.example.quicknotes.Navigation.CreateEditBottomBar
import com.example.quicknotes.Navigation.CreateEditNoteTopBar
import com.example.quicknotes.ViewModel.NoteViewModel

class CreateNoteActivity : ComponentActivity() {

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        enableEdgeToEdge()
        setContent {

            val isSaveEnabled by remember { mutableStateOf(true) }
            val isCancelEnabled by remember { mutableStateOf(true) }
            var isFavorite by remember { mutableStateOf(false) }
            var title by remember { mutableStateOf("") }
            var note by remember { mutableStateOf("") }


            QuickNotesTheme {
                Scaffold(
                    topBar = {
                        CreateEditNoteTopBar(
                            onFavClick = {isFavorite = !isFavorite},

                            isFavorite = isFavorite
                        )
                    },
                    bottomBar = {
                        CreateEditBottomBar(
                            onSaveClick = {
                                if (title.isNotEmpty() && note.isNotEmpty()) {
                                    val newNote = Note(
                                        id = System.currentTimeMillis(),
                                        title = title,
                                        note = note,
                                        isFav = isFavorite
                                    )

                                    noteViewModel.addNewNote(newNote) // Add new note to the ViewModel
                                    title = "" // Clear the title input
                                    note = "" // Clear the note content input

                                    Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show()
                                    finish()
                                    Log.d("CreateNoteActivity", "New note created: $newNote")

                                } else {
                                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                                }
                            }
                            ,
                            onCancelClick = {finish()},
                            isSaveEnabled = isSaveEnabled,
                            isCancelEnabled = isCancelEnabled
                        )
                    },
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .padding(22.dp, innerPadding.calculateTopPadding())
                    ) {
                        CustomTextField(
                            text = title,
                            onTextChange = { newText ->
                                title = newText // Update title state
                            }
                        )

                        Spacer(
                            modifier = Modifier
                                .height(1.dp).fillMaxWidth()
                                .background(Color.Gray)
                                .padding(vertical = 28.dp)
                        )

                        CustomNoteField(
                            text = note,
                            onTextChange = { newText ->
                                note = newText // Update title state
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    text: String,
    onTextChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth().padding(vertical = 18.dp)
            .background(Color.Black) // Set background color to black
    ) {
        if (text.isEmpty()) {
            Text(
                text = "Note Title", // Placeholder text
                color = Color.Gray,
                fontSize = 22.sp, // Set font size
            )
        }
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = TextStyle(
                color = Color.White, // Text color inside the text field
                fontSize = 22.sp,
            ),
            cursorBrush = SolidColor(Color.White), // Explicitly set the cursor color

        )
    }
}


@Composable
fun CustomNoteField(
    text: String,
    onTextChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(vertical = 28.dp).verticalScroll(rememberScrollState())
            .background(Color.Black)
            .imePadding() // Adjust for keyboard
    ) {
        if (text.isEmpty()) {
            Text(
                text = "Type Note Here...", // Placeholder text
                color = Color.Gray,
                fontSize = 20.sp, // Set font size
            )
        }
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = TextStyle(
                color = Color.White, // Text color inside the text field
                fontSize = 22.sp,
            ),
            cursorBrush = SolidColor(Color.White), // Explicitly set the cursor color
            modifier = Modifier.fillMaxSize().height(540.dp)
        )

    }
}
