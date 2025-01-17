package com.example.quicknotes.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quicknotes.Activities.ui.theme.QuickNotesTheme
import com.example.quicknotes.Model.Note
import com.example.quicknotes.Navigation.CreateEditBottomBar
import com.example.quicknotes.Navigation.CreateEditNoteTopBar
import com.example.quicknotes.ViewModel.NoteViewModel
class ViewEditNoteActivity : ComponentActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private var noteId: Long = -1 // Default to -1 in case the intent does not pass a valid ID.
    private var noteTitle: String = ""
    private var noteContent: String = ""
    private var isFav: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the noteId from the Intent
        noteId = intent.getLongExtra("noteId", -1L) // Default to -1 if the noteId is not passed
        Log.d("ViewEditNote", "Received noteId: $noteId") // Log to verify the noteId

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // Fetch the note data using noteId from the ViewModel
        noteViewModel.getNoteById(noteId)?.observe(this, Observer { note ->
            if (note != null) {
                noteTitle = note.title
                noteContent = note.note
                isFav = note.isFav
            } else {
                // Handle case when the note is not found (e.g., show an error message)
                Toast.makeText(this, "Note not found", Toast.LENGTH_SHORT).show()
            }
        })

        enableEdgeToEdge()

        setContent {
            val isSaveEnabled by remember { mutableStateOf(true) }
            val isCancelEnabled by remember { mutableStateOf(true) }
            var isFavorite by remember { mutableStateOf(isFav) }
            var title by remember { mutableStateOf(noteTitle) }
            var note by remember { mutableStateOf(noteContent) }

            QuickNotesTheme {
                Scaffold(
                    topBar = {
                        CreateEditNoteTopBar(
                            isFavorite = isFavorite, // Pass the state to the top bar
                            onFavClick = {
                                isFavorite = !isFavorite // Toggle the favorite state when clicked
                            }
                        )
                    },
                    bottomBar = {
                        CreateEditBottomBar(
                            onSaveClick = {
                                if (title.isNotEmpty() && note.isNotEmpty()) {
                                    val updatedNote = Note(
                                        id = noteId,
                                        title = title,
                                        note = note,
                                        isFav = isFavorite
                                    )

                                    noteViewModel.updateNote(updatedNote) // Update the note in the ViewModel
                                    Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
                                    finish()
                                    Log.d("EditNoteActivity", "Updated note: $updatedNote")
                                } else {
                                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onCancelClick = { finish() },
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
                            onTextChange = { newText -> title = newText }
                        )

                        Spacer(
                            modifier = Modifier
                                .height(1.dp).fillMaxWidth()
                                .background(Color.Gray)
                                .padding(vertical = 28.dp)
                        )

                        CustomNoteField(
                            text = note,
                            onTextChange = { newText -> note = newText }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Check for any changes or updates that might have been made in ViewEditActivity
        noteViewModel.loadNotes()// Ensure the ViewModel is up-to-date
    }
}