package com.amitranofinzi.vimata.ui.screen.athlete

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.components.AthleteBNavBar
import com.amitranofinzi.vimata.ui.navigation.AthleteBottomNav
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.ChatViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AthleteScreen(athleteViewModel: AthleteViewModel = AthleteViewModel(),
                  authViewModel: AuthViewModel = AuthViewModel(),
                  chatViewModel: ChatViewModel = ChatViewModel(),
                  navController: NavController) {

        val bottomNavController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomAppBar {
                    AthleteBNavBar(bottomNavController)
                }
            })
            { AthleteBottomNav(athleteViewModel, authViewModel, chatViewModel, navController, bottomNavController) }
    }

/*
@Preview(showBackground = true)
@Composable
fun PreviewAthleteScreen() {
    VimataTheme {
        AthleteScreen()
    }
}
*/