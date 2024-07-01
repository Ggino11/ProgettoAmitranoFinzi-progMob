package com.amitranofinzi.vimata.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email

sealed class AthleteBNavItem {
    object Home :
        Item(path = AthleteBNavNode.HOME.toString(), title = AthleteBNavTitle.HOME, icon = Icons.Default.Home)

    object Chat :
        Item(path = AthleteBNavNode.CHAT.toString(), title = AthleteBNavTitle.CHAT, icon = Icons.Default.Email)

    object Progress :
        Item(path = AthleteBNavNode.PROGRESS.toString(), title = AthleteBNavTitle.PROGRESS, icon = Icons.Default.Star)

    object Profile :
        Item(
            path = AthleteBNavNode.PROFILE.toString(), title = AthleteBNavTitle.PROFILE, icon = Icons.Default.Person)
}