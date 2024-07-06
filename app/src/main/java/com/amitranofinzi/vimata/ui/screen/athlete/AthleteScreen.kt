package com.amitranofinzi.vimata.ui.screen.athlete

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.components.AthleteBNavBar
import com.amitranofinzi.vimata.ui.navigation.AthleteBottomNav
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AuthViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AthleteScreen(authViewModel: AuthViewModel = AuthViewModel(), navController: NavController) {
        val bottomNavController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomAppBar {
                    AthleteBNavBar(bottomNavController)
                }
            })
            { AthleteBottomNav(authViewModel, navController, bottomNavController) }
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