package com.amitranofinzi.vimata.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteChatScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteHomeScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProfileScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProgressScreen


@Composable
fun AthleteAppNav() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AthleteBNavItem.Home.path) {

        composable(route = AthleteBNavItem.Home.path) { AthleteHomeScreen() }
        composable(AthleteBNavItem.Progress.path) { AthleteProgressScreen() }
        composable(AthleteBNavItem.Chat.path) { AthleteChatScreen() }
        composable(AthleteBNavItem.Profile.path) { AthleteProfileScreen() }

    }
}
