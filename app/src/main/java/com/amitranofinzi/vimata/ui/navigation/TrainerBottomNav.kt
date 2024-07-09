package com.amitranofinzi.vimata.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amitranofinzi.vimata.ui.screen.chat.ListChatScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerHomeScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerProfileScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerWorkbookScreen
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@Composable
fun TrainerBottomNav(trainerViewModel: TrainerViewModel = TrainerViewModel(),
                     authViewModel: AuthViewModel = AuthViewModel(),
                     navController: NavController,
                     bottomNavController: NavHostController) {

    NavHost(navController = bottomNavController, startDestination = TrainerBNavItem.Home.path) {
        composable(TrainerBNavItem.Home.path) { TrainerHomeScreen() }
        composable(TrainerBNavItem.Workbook.path) { TrainerWorkbookScreen() }
        composable(TrainerBNavItem.Chat.path) { ListChatScreen() }
        composable(TrainerBNavItem.Profile.path) {

            TrainerProfileScreen(onEditProfileClick = {}, trainerViewModel, authViewModel, navController)
        }
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

