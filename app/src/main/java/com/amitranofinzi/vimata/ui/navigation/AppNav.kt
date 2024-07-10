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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteScreen
import com.amitranofinzi.vimata.ui.screen.auth.LoginScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerWorkbookScreen
import com.amitranofinzi.vimata.ui.screen.viewer.CollectionViewerScreen
import com.amitranofinzi.vimata.ui.screen.viewer.TestSetViewerScreen
import com.amitranofinzi.vimata.ui.screens.SignUpScreen
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@Composable
fun AppNav() {

    val navController = rememberNavController()

    //com
    // NESTED GRAPH CONSTRUCTOR
    // Vengono in questa funzione tutti i grafi di navigazione principali
    // definiti ognuno tramite una funzione dedicata
    // Per ognuno di essi viene definito uno shared viewModel
    NavHost(navController = navController, startDestination = "auth") {
        authGraph(navController)
        athleteGraph(navController)
        trainerGraph(navController)

    }

}
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation( startDestination = "login", route = "auth"){

        composable("login"){
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            LoginScreen(authViewModel = authViewModel, navController = navController);
        }
        composable("signup"){
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            SignUpScreen(authViewModel = authViewModel, navController = navController);
        }
        composable("forgot_psw"){
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
        }
    }
}
fun NavGraphBuilder.athleteGraph(navController: NavHostController) {

    navigation(
        startDestination = "athlete_screen",
        route = "athlete"
    ){
        composable("athlete_screen"){
            val athleteViewModel = it.sharedViewModel<AthleteViewModel>(navController)
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            AthleteScreen(athleteViewModel,authViewModel,navController)
        }
        composable("testSetDetails/{testSetId}",listOf( navArgument("testSetId") { type = NavType.StringType  })
        ){
            // Ottieni l'ID del test set dalla navigazione
            val testSetId = it.arguments?.getString("testSetId")
            Log.d("AppNav", testSetId.isNullOrEmpty().toString())
            val athleteViewModel = it.sharedViewModel<AthleteViewModel>(navController)
            // Visualizza i dettagli del test set con l'ID specificato
            TestSetViewerScreen(athleteViewModel,testSetId, navController)
        }
        /*
        composable("chatDetails/{chatId}", listOf( navArgument("chatId") ){ type = NavType.StringType}){
            val chatId = it.arguments?.getString("chatId")
            val athleteViewModel = it.sharedViewModel<AthleteViewModel>(navController)
            singleChatScreen(athleteViewModel,chatId, navController)
        }
        */

    }

}

fun NavGraphBuilder.trainerGraph(navController: NavHostController) {

    navigation(
        startDestination = "trainer_screen",
        route = "trainer"
    ){
        composable("trainer_screen"){
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController)
            TrainerScreen(trainerViewModel,authViewModel, navController)
        }
        composable("collection/{collectionID}",listOf( navArgument("collectionID") { type = NavType.StringType  })
        ){
            // Ottieni l'ID del test set dalla navigazione
            val collectionID = it.arguments?.getString("collectionID")
            Log.d("AppNav-TrainerGraph", collectionID.isNullOrEmpty().toString())

            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController)
            // Visualizza i dettagli del test set con l'ID specificato
            CollectionViewerScreen(trainerViewModel,collectionID, navController)
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