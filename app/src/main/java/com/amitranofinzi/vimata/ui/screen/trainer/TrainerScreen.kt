package com.amitranofinzi.vimata.ui.screen.trainer

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.components.AthleteBNavBar
import com.amitranofinzi.vimata.ui.components.TrainerBNavBar
import com.amitranofinzi.vimata.ui.navigation.AthleteBottomNav
import com.amitranofinzi.vimata.ui.navigation.TrainerBottomNav
import com.amitranofinzi.vimata.ui.theme.VimataTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainerScreen() {
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomAppBar {
                TrainerBNavBar(bottomNavController)
            }
        })
    { TrainerBottomNav(bottomNavController) }
}


@Preview(showBackground = true)
@Composable
fun PreviewTrainerScreen() {
    VimataTheme {
        TrainerScreen()
    }
}
