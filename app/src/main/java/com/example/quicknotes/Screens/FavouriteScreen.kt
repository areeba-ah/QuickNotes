package com.example.quicknotes.Screens

import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quicknotes.Activities.NoteCard
import com.example.quicknotes.Activities.ViewEditNoteActivity
import com.example.quicknotes.Model.Note
import com.example.quicknotes.R
import com.example.quicknotes.ViewModel.NoteViewModel


@Composable
fun FavouriteScreen(vm: NoteViewModel) {
    val context = LocalContext.current
    val notes by vm.notes.observeAsState(emptyList()) // Using LiveData with observeAsState

    // Filter favorite notes
    val favoriteNotes = notes.filter { it.isFav }

    //  val noteList = vm.stateList // Automatically observes changes
    Log.d("HomeScreen", "Current note count: ${notes.size}")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (favoriteNotes.isEmpty()) {
            item {

                // Display message when the list is empty
                Box(
                    modifier = Modifier
                        .fillParentMaxSize() // Use fillParentMaxSize for LazyColumn
                        .background(Color.Black), // Ensure a contrasting background
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.noFavItem).uppercase(),
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {

            // Assuming `notes` is a list of notes and each note has a property `isFav`
            items(notes.filter { it.isFav }) { note ->
                FavCard(
                    note = note,
                    onCardClick = {
                        // Print the note ID to log
                        Log.d("NoteCard", "Note ID: ${note.id}")

                        val intent = Intent(context, ViewEditNoteActivity::class.java)
                        intent.putExtra("noteId", note.id) // Pass the note ID
                        context.startActivity(intent)
                    },
                    removeFromFav = {
                        // Update the note's isFav property to false
                        val updatedNote = note.copy(isFav = false) // Create a copy of the note with isFav set to false

                        // Update the note in the ViewModel (or database)
                        vm.updateNote(updatedNote)

                        Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}



@Composable
fun FavCard(note: Note, onCardClick: () -> Unit, removeFromFav: () -> Unit) {
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
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Note title
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f)) // Space between title and content


                IconButton(onClick = removeFromFav) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Remove Note From Fav")
                }
            }

            // Note content with truncation and ellipsis
            Text(
                text = note.note,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray, // Color for note content text
                maxLines = 3, // Limit the content to 3 lines
                overflow = TextOverflow.Ellipsis, // Truncate and add ellipsis if text overflows
            )

        }
    }
}