package com.example.quicknotes.Navigation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyBottomBar(selectedScreen: MutableState<NavItem>) {
    val navItems = listOf(NavItem.Home, NavItem.Favourite)

    NavigationBar(
        containerColor = Color.Black
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = item == selectedScreen.value,
                onClick = { selectedScreen.value = item
                },

                label = {
                    Text(
                        text = item.title,
                        color = if (item == selectedScreen.value) Color.White else Color.Gray
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (item == selectedScreen.value) Color.White else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF03DAC5),
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}
@Composable
fun CreateEditBottomBar(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    isSaveEnabled: Boolean,  // Flag for Save button's state
    isCancelEnabled: Boolean // Flag for Cancel button's state
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(28.dp), // Add padding to the Row
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Cancel Button
        TextButton(
            onClick = { onCancelClick() },
            enabled = isCancelEnabled, // Enable or disable based on state
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Cancel",
                color = Color.LightGray, // Change color based on state
                style = MaterialTheme.typography.titleLarge // Make text bigger
            )
        }

        // Save Button
        TextButton(
            onClick = { onSaveClick() },
            enabled = isSaveEnabled, // Enable or disable based on state
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Save",
                color = Color.LightGray, // Change color based on state
                style = MaterialTheme.typography.titleLarge // Make text bigger
            )
        }
    }
}