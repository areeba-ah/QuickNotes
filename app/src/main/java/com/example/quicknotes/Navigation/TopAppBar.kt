package com.example.quicknotes.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.quicknotes.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(){
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = androidx.compose.ui.res.colorResource(id = R.color.topBar) // Use custom color for title text
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Black, // Set the background color
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditNoteTopBar(onFavClick: () -> Unit, isFavorite: Boolean){
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = androidx.compose.ui.res.colorResource(id = R.color.topBar) // Use custom color for title text
            )
        },

        actions = {
            IconButton(onClick = onFavClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Search Action",
                    tint = Color.White // Custom color for action button
                )
            }
        },

        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Black, // Set the background color
        )
    )
}
