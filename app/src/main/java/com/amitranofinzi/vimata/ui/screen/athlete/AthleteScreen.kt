package com.amitranofinzi.vimata.ui.screen.athlete

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.amitranofinzi.vimata.ui.components.AthleteBNavBar
import com.amitranofinzi.vimata.ui.navigation.AthleteAppNav
import com.amitranofinzi.vimata.ui.navigation.AthleteNavScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AthleteScreen(navController: NavHostController) {
        Scaffold(bottomBar = {
            BottomAppBar { AthleteBNavBar(navController = navController) }
        }) { AthleteAppNav(navController = navController) }
    }