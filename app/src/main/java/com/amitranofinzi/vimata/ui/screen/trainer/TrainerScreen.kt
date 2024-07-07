package com.amitranofinzi.vimata.ui.screen.trainer

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.components.TrainerBNavBar
import com.amitranofinzi.vimata.ui.navigation.TrainerBottomNav
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainerScreen(trainerViewModel: TrainerViewModel = TrainerViewModel(),
                  authViewModel: AuthViewModel = AuthViewModel(),
                  navController: NavController) {
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomAppBar {
                TrainerBNavBar(bottomNavController)
            }
        })
    { TrainerBottomNav(trainerViewModel, authViewModel, navController, bottomNavController) }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewTrainerScreen() {
    VimataTheme {
        TrainerScreen()
    }
}
*/