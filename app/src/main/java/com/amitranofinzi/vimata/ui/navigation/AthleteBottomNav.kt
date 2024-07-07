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
        composable(AthleteBNavItem.Home.path) { AthleteHomeScreen() }
        composable(AthleteBNavItem.Progress.path) { AthleteProgressScreen() }
        composable(AthleteBNavItem.Chat.path) { AthleteChatScreen() }
        composable(AthleteBNavItem.Profile.path) {
            AthleteProfileScreen(onEditProfileClick = {}, athleteViewModel,authViewModel, navController) }
    }

}

/*
fun NavGraphBuilder.athleteBottomNavGraph(navController: NavController) {

        navigation(
            startDestination = AthleteBNavItem.Home.path,
            route = "athlete"
        ){

        }

}
*/

