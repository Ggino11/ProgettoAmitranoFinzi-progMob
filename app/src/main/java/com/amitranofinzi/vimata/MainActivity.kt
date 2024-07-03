package com.amitranofinzi.vimata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.navigation.AthleteAppNav
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteScreen
import com.amitranofinzi.vimata.ui.screen.auth.LoginScreen
import com.amitranofinzi.vimata.ui.theme.VimataTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            // Applying style theme on every UI component
            VimataTheme {
                AthleteScreen(navController)
                //LoginScreen()
            }


        }
    }
}

