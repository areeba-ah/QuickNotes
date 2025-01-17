package com.example.quicknotes.ViewModel
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.quicknotes.Model.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _notes = MutableLiveData<List<Note>>(emptyList())
    val notes: LiveData<List<Note>> get() = _notes

    init {
        loadNotes() // Load notes when ViewModel is initialized
    }

    // Retrieve a single note by its ID from SharedPreferences
    fun getNoteById(noteId: Long): LiveData<Note?> {
        return liveData {
            val note = _notes.value?.find { it.id == noteId } // Find note with the matching ID
            emit(note)
        }
    }

    // Load notes from SharedPreferences
    fun loadNotes() {
        val json = sharedPreferences.getString("notes_list", null)
        if (json != null) {
            val type = object : TypeToken<List<Note>>() {}.type
            val savedNotes: List<Note> = gson.fromJson(json, type)
            _notes.value = savedNotes
        }
    }

    // Function to add a new note
    fun addNewNote(note: Note) {
        val currentList = _notes.value?.toMutableList() ?: mutableListOf()
        currentList.add(note) // Add new note
        _notes.value = currentList // Set updated list

        saveNotes() // Save updated list to SharedPreferences
        Log.d("NoteViewModel", "New note added: $note, ${currentList.size}")
    }

    // Function to update an existing note
    fun updateNote(note: Note) {
        val currentList = _notes.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == note.id } // Find the index of the note by ID
        if (index != -1) {
            currentList[index] = note // Update the note at that index
            _notes.value = currentList // Set the updated list back to LiveData

            saveNotes() // Save the updated list to SharedPreferences
            Log.d("NoteViewModel", "Note updated: $note")
        } else {
            Log.e("NoteViewModel", "Note not found for update: $note")
        }
    }

    // Function to delete a note
    fun deleteNote(note: Note) {
        val currentList = _notes.value?.toMutableList() ?: mutableListOf()
        currentList.remove(note) // Remove note
        _notes.value = currentList // Set updated list

        saveNotes() // Save updated list to SharedPreferences
        Log.d("NoteViewModel", "Deleted note: $note")
    }

    // Save notes list to SharedPreferences
    private fun saveNotes() {
        val json = gson.toJson(_notes.value)
        sharedPreferences.edit().putString("notes_list", json).apply()
    }
}