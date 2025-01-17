package com.example.quicknotes.Shared

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.quicknotes.ViewModel.NoteViewModel

class MyApplication : Application() {
    val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this)
            .create(NoteViewModel::class.java)
    }
}