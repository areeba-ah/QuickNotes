package com.example.quicknotes.Model
import java.io.Serializable

data class Note(
    val id: Long,
    var title: String,
    var note: String,
    var isFav: Boolean
) : Serializable


