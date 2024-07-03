package com.amitranofinzi.vimata.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteChatScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteHomeScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProfileScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProgressScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteScreen
import com.amitranofinzi.vimata.ui.screen.auth.LoginScreen
import com.amitranofinzi.vimata.ui.screen.auth.SignUpScreen
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AuthViewModel

@Composable
fun AthleteBottomNav(navController: NavHostController) {

    NavHost(navController = navController, startDestination = AthleteBNavItem.Home.path) {
        composable(AthleteBNavItem.Home.path) { AthleteHomeScreen() }
        composable(AthleteBNavItem.Progress.path) { AthleteProgressScreen() }
        composable(AthleteBNavItem.Chat.path) { AthleteChatScreen() }
        composable(AthleteBNavItem.Profile.path) { AthleteProfileScreen() }
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
