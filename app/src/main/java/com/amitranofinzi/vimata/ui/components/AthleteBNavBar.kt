package com.amitranofinzi.vimata.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.amitranofinzi.vimata.ui.navigation.AthleteBNavItem

@Composable
fun AthleteBNavBar(navController: NavController) {
    val navItems = listOf(
        AthleteBNavItem.Home,
        AthleteBNavItem.Progress,
        AthleteBNavItem.Chat,
        AthleteBNavItem.Profile
    )
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = {
                        when (item) {
                            AthleteBNavItem.Home -> Icon(Icons.Default.Home, contentDescription = "Home")
                            AthleteBNavItem.Progress -> Icon(Icons.Default.AutoGraph, contentDescription = "Progress")
                            AthleteBNavItem.Chat -> Icon(Icons.Default.ChatBubble, contentDescription = "Chat")
                            AthleteBNavItem.Profile -> Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                       },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.path) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
