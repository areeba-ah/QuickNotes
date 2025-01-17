package com.example.quicknotes.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavItem (val title: String, val icon: ImageVector) {
    object Home : NavItem("home",Icons.Default.Home)
    object Favourite : NavItem("favourite",Icons.Default.Favorite)
}