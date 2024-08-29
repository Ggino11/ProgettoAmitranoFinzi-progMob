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
import com.amitranofinzi.vimata.viewmodel.ChatViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

/**
 * Sets up the bottom navigation for the trainer section of the app using Jetpack Compose's NavHost.
 * This function defines the navigation routes for the trainer-specific screens such as home,
 * workbook, chat, and profile.
 *
 * @param trainerViewModel The ViewModel for managing trainer-specific data and state.
 * @param authViewModel The ViewModel for managing authentication-related data and state.
 * @param chatViewModel The ViewModel for managing chat-related data and state.
 * @param navController The main navigation controller used to manage global navigation.
 * @param bottomNavController The NavHostController used for managing the bottom navigation destinations.
 */
@Composable
fun TrainerBottomNav(trainerViewModel: TrainerViewModel = TrainerViewModel(),
                     authViewModel: AuthViewModel = AuthViewModel(),
                     chatViewModel: ChatViewModel = ChatViewModel(),
                     navController: NavController,
                     bottomNavController: NavHostController) {

    NavHost(navController = bottomNavController, startDestination = TrainerBNavItem.Home.path) {

        composable(TrainerBNavItem.Home.path) {
            TrainerHomeScreen(trainerViewModel, authViewModel, navController)
        }

        composable(TrainerBNavItem.Workbook.path) {
            TrainerWorkbookScreen(trainerViewModel, authViewModel, navController)
        }
        composable(TrainerBNavItem.Chat.path) {
            ListChatScreen(chatViewModel,authViewModel,navController)
        }

        composable(TrainerBNavItem.Profile.path) {
            TrainerProfileScreen(onEditProfileClick = {}, trainerViewModel, authViewModel, navController)
        }
    }

}


