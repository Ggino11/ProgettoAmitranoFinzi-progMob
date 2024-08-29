package com.amitranofinzi.vimata.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteHomeScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProfileScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProgressScreen
import com.amitranofinzi.vimata.ui.screen.chat.ListChatScreen
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.ChatViewModel

/**
 * Sets up the bottom navigation for the athlete section of the app using Jetpack Compose's NavHost.
 * This function defines the navigation routes for the athlete-specific screens such as home,
 * progress, chat, and profile.
 *
 * @param athleteViewModel The ViewModel for managing athlete-specific data and state.
 * @param authViewModel The ViewModel for managing authentication-related data and state.
 * @param chatViewModel The ViewModel for managing chat-related data and state.
 * @param navController The main navigation controller used to manage global navigation.
 * @param bottomNavController The NavHostController used for managing the bottom navigation destinations.
 */
@Composable
fun AthleteBottomNav(athleteViewModel: AthleteViewModel = AthleteViewModel(),
                     authViewModel: AuthViewModel = AuthViewModel(),
                     chatViewModel: ChatViewModel = ChatViewModel(),
                     navController: NavController,
                     bottomNavController: NavHostController) {
    NavHost(navController = bottomNavController, startDestination = AthleteBNavItem.Home.path) {
        composable(AthleteBNavItem.Home.path) {
            Log.d("athlete bottom nav", "nel bottom nav")
            AthleteHomeScreen(athleteViewModel,authViewModel,navController)
        }
        composable(AthleteBNavItem.Progress.path) {
            AthleteProgressScreen(athleteViewModel, authViewModel, navController)
        }
        composable(AthleteBNavItem.Chat.path) {
            ListChatScreen(chatViewModel,authViewModel,navController)
        }
        composable(AthleteBNavItem.Profile.path) {
            AthleteProfileScreen(onEditProfileClick = {}, athleteViewModel,authViewModel, navController) }
    }

}



