package com.example.quicknotes.Navigation
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.quicknotes.Activities.CreateNoteActivity
import com.example.quicknotes.ViewModel.NoteViewModel

@Composable
fun MyFavButton(vm: NoteViewModel) {
    val context = LocalContext.current

    FloatingActionButton(onClick = {
        val intent = Intent(context, CreateNoteActivity::class.java)
        context.startActivity(intent) // Start CreateNoteActivity
    } , content = {
        Icon(Icons.Default.Add, contentDescription = "Create New Note.")
    })

}