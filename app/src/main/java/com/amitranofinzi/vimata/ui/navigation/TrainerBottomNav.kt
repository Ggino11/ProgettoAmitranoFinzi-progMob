package com.amitranofinzi.vimata.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerChatScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerHomeScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerProfileScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerWorkbookScreen

@Composable
fun TrainerBottomNav(navController: NavHostController) {

    NavHost(navController = navController, startDestination = TrainerBNavItem.Home.path) {
        composable(TrainerBNavItem.Home.path) { TrainerHomeScreen() }
        composable(TrainerBNavItem.Workbook.path) { TrainerWorkbookScreen() }
        composable(TrainerBNavItem.Chat.path) { TrainerChatScreen() }
        composable(TrainerBNavItem.Profile.path) { TrainerProfileScreen() }
    }

}

/*
fun NavGraphBuilder.TrainerBottomNavGraph(navController: NavController) {

        navigation(
            startDestination = TrainerBNavItem.Home.path,
            route = "Trainer"
        ){

        }

}
*/

