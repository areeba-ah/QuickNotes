package com.example.quicknotes.Model

interface NoteInterface {
    fun addNewNoteStatically(note:Note): ArrayList<Note>
    fun updateExistingNote(note:Note)
}