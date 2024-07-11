package com.amitranofinzi.vimata.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.navigation.AthleteBNavItem
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.TextColor

@Composable
fun AthleteBNavBar(navController: NavController) {
    val navItems = listOf(
        AthleteBNavItem.Home,
        AthleteBNavItem.Progress,
        AthleteBNavItem.Chat,
        AthleteBNavItem.Profile
    )
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    NavigationBar(
        containerColor = BgColor,
        contentColor = TextColor,
        tonalElevation = 8.dp
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = {
                    when (item) {
                        AthleteBNavItem.Home -> Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            tint = if (selectedItem == index) Primary else TextColor
                        )
                        AthleteBNavItem.Progress -> Icon(
                            Icons.Default.AutoGraph,
                            contentDescription = "Progress",
                            tint = if (selectedItem == index) Primary else TextColor
                        )
                        AthleteBNavItem.Chat -> Icon(
                            Icons.Default.ChatBubble,
                            contentDescription = "Chat",
                            tint = if (selectedItem == index) Primary else TextColor
                        )
                        AthleteBNavItem.Profile -> Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = if (selectedItem == index) Primary else TextColor
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selectedItem == index) Primary else TextColor
                    )
                },
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
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    unselectedIconColor = TextColor,
                    selectedTextColor = Primary,
                    unselectedTextColor = TextColor,
                    indicatorColor = Primary.copy(alpha = 0.1f)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AthleteBNavBarPreview() {
    AthleteBNavBar(navController = rememberNavController())
}
