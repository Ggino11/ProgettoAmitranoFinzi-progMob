package com.amitranofinzi.vimata.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteChatScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteHomeScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProfileScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProgressScreen
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel

@Composable
fun AthleteBottomNav(athleteViewModel: AthleteViewModel = AthleteViewModel(),
                     authViewModel: AuthViewModel = AuthViewModel(),
                     navController: NavController,
                     bottomNavController: NavHostController) {
    NavHost(navController = bottomNavController, startDestination = AthleteBNavItem.Home.path) {
        composable(AthleteBNavItem.Home.path) { AthleteHomeScreen(athleteViewModel,authViewModel,navController) }
        composable(AthleteBNavItem.Progress.path) {
            AthleteProgressScreen(athleteViewModel, authViewModel, navController)
        }
        composable(AthleteBNavItem.Chat.path) { AthleteChatScreen() }
        composable(AthleteBNavItem.Profile.path) {
            AthleteProfileScreen(onEditProfileClick = {}, athleteViewModel,authViewModel, navController) }
    }

}



