package com.example.quicknotes.Navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object CreateNote : Screen("create_note")
    object ViewEditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Long) = "edit_note/$noteId"
    }
}