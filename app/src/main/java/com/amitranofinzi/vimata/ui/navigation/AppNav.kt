package com.amitranofinzi.vimata.ui.navigation
import android.util.Log

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteChatScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteHomeScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProfileScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteProgressScreen
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteScreen
import com.amitranofinzi.vimata.ui.screen.auth.LoginScreen
import com.amitranofinzi.vimata.ui.screen.auth.SignUpScreen
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AuthViewModel




@Composable
fun AppNav() {

    val navController = rememberNavController()

    // NESTED GRAPH CONSTRUCTOR
    // Vengono in questa funzione tutti i grafi di navigazione principali
    // definiti ognuno tramite una funzione dedicata
    // Per ognuno di essi viene definito uno shared viewModel
    NavHost(navController = navController, startDestination = "auth") {
        authGraph(navController)
        athleteGraph(navController)
    }

}
fun NavGraphBuilder.authGraph(navController: NavHostController) {
        navigation( startDestination = "login", route = "auth"){
            composable("login"){
                //val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                LoginScreen(/*loginViewModel = viewModel,*/ navController = navController);
            }
            composable("signup"){
                //val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                SignUpScreen(/*loginViewModel = viewModel,*/ navController = navController);
            }
            composable("forgot_psw"){
                //val viewModel = it.sharedViewModel<AuthViewModel>(navController)
            }
        }
}
fun NavGraphBuilder.athleteGraph(navController: NavHostController) {

        navigation(
            startDestination = "athlete_screen",
            route = "athlete"
        ){
            composable("athlete_screen"){
                AthleteScreen()
            }
        }

}


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}


@Preview(showBackground = true)
@Composable
fun PreviewAppNav(){

    VimataTheme {
        AppNav()
    }
}