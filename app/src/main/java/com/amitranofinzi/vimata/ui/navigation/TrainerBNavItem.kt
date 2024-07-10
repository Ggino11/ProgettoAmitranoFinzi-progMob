package com.amitranofinzi.vimata.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star

sealed class TrainerBNavItem {
    object Home :
        Item(path = TrainerBNavNode.HOME.toString(), title = NavRoutes.HOME, icon = Icons.Default.Home)

    object Workbook :
        Item(path = TrainerBNavNode.WORKBOOK.toString(), title = NavRoutes.WORKBOOK, icon = Icons.Default.Book)

    object Chat :
        Item(path = TrainerBNavNode.CHAT.toString(), title = NavRoutes.CHAT, icon = Icons.Default.Email)

    object Profile :
        Item(
            path = TrainerBNavNode.PROFILE.toString(), title = NavRoutes.PROFILE, icon = Icons.Default.Person)
}