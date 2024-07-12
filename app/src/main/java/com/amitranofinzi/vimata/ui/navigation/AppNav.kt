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
import com.amitranofinzi.vimata.ui.screen.chat.SingleChatScreen
import com.amitranofinzi.vimata.ui.screen.editor.ExerciseEditorScreen
import com.amitranofinzi.vimata.ui.screen.editor.ExerciseSelectionScreen
import com.amitranofinzi.vimata.ui.screen.sensing.VideoPlayerScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerScreen
import com.amitranofinzi.vimata.ui.screen.viewer.AthleteHandlerViewerScreen
import com.amitranofinzi.vimata.ui.screen.viewer.CollectionViewerScreen
import com.amitranofinzi.vimata.ui.screen.viewer.TestSetEditorScreen
import com.amitranofinzi.vimata.ui.screen.viewer.TestSetViewerScreen
import com.amitranofinzi.vimata.ui.screens.SignUpScreen
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.CameraViewModel
import com.amitranofinzi.vimata.viewmodel.ChatViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel
import com.example.yourapp.CameraScreen
import java.net.URLDecoder

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
            val chatViewModel = it.sharedViewModel<ChatViewModel>(navController)

            AthleteScreen(athleteViewModel, authViewModel, chatViewModel, navController)
        }
        composable("testSetDetails/{testSetId}",listOf( navArgument("testSetId") { type = NavType.StringType  })
        ){
            // Ottieni l'ID del test set dalla navigazione
            val testSetId = it.arguments?.getString("testSetId")
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            val athleteViewModel = it.sharedViewModel<AthleteViewModel>(navController)
            val cameraViewModel = it.sharedViewModel<CameraViewModel>(navController)
            // Visualizza i dettagli del test set con l'ID specificato
            TestSetViewerScreen(authViewModel, athleteViewModel,cameraViewModel,testSetId, navController)
        }

        composable("chatDetails/{chatId}", listOf( navArgument("chatId") { type = NavType.StringType})
        ){
            val chatId = it.arguments?.getString("chatId")
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            val chatViewModel = it.sharedViewModel<ChatViewModel>(navController)

            SingleChatScreen(authViewModel,chatViewModel,chatId, navController)
        }
        composable("cameraScreen/{testID}", listOf( navArgument("testID") { type = NavType.StringType})){
            Log.d("AppNav","dentro cameraScreen route")
            val testID = it.arguments?.getString("testID")

            val cameraViewModel = it.sharedViewModel<CameraViewModel>(navController)
            if (testID != null) {
                CameraScreen(cameraViewModel, testID, navController)
            }
        }
        composable("videoPlayer/{videoUrl}", listOf( navArgument("videoUrl") { type = NavType.StringType})){
            Log.d("AppNav","dentro video player route")
            val videoUrl = it.arguments?.getString("videoUrl")?.let { url -> URLDecoder.decode(url, "UTF-8") }

            if (videoUrl != null) {
                VideoPlayerScreen(videoUrl)
            }
        }


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
            val chatViewModel = it.sharedViewModel<ChatViewModel>(navController)
            TrainerScreen(trainerViewModel,authViewModel,chatViewModel, navController)
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
        composable("athleteHandler/{athleteID}",listOf( navArgument("athleteID") { type = NavType.StringType  })
        ){
            // Ottieni l'ID del test set dalla navigazione
            val athleteID = it.arguments?.getString("athleteID")
            Log.d("AppNav-TrainerGraph", athleteID.isNullOrEmpty().toString())

            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController)
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)

            // Visualizza i dettagli del test set con l'ID specificato
            AthleteHandlerViewerScreen(trainerViewModel,authViewModel,athleteID, navController)
        }
        composable("testSetEditor/{athleteID}",listOf( navArgument("athleteID") { type = NavType.StringType  })
        ){
            val athleteID = it.arguments?.getString("athleteID")
            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController)
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)

            TestSetEditorScreen(authViewModel,trainerViewModel,athleteID!!,navController)
        }
        composable("exerciseEditor/{trainerID}",listOf( navArgument("trainerID") { type = NavType.StringType  }))
        {
            val trainerID = it.arguments?.getString("trainerID")
            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController)
            if (trainerID != null) {
                ExerciseEditorScreen(trainerViewModel, trainerID, navController)
            }
        }
        composable("exerciseSelection/{trainerID}",listOf( navArgument("trainerID") { type = NavType.StringType  }))
        {
            val trainerID = it.arguments?.getString("trainerID")
            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController)
            if (trainerID != null) {
                ExerciseSelectionScreen(trainerViewModel, trainerID, navController)
            }
        }
        composable("testSetDetails/{testSetId}",listOf( navArgument("testSetId") { type = NavType.StringType  })
        ){
            // Ottieni l'ID del test set dalla navigazione
            val testSetId = it.arguments?.getString("testSetId")
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            val athleteViewModel = it.sharedViewModel<AthleteViewModel>(navController)
            val cameraViewModel = it.sharedViewModel<CameraViewModel>(navController)
            // Visualizza i dettagli del test set con l'ID specificato
            TestSetViewerScreen(authViewModel, athleteViewModel,cameraViewModel,testSetId, navController)
        }
        composable("chatDetails/{chatId}", listOf( navArgument("chatId") { type = NavType.StringType})
        ){
            val chatId = it.arguments?.getString("chatId")
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            val chatViewModel = it.sharedViewModel<ChatViewModel>(navController)

            SingleChatScreen(authViewModel,chatViewModel,chatId, navController)
        }
        composable("cameraScreen/{testID}", listOf( navArgument("testID") { type = NavType.StringType})){
            Log.d("AppNav","dentro cameraScreen route")
            val testID = it.arguments?.getString("testID")

            val cameraViewModel = it.sharedViewModel<CameraViewModel>(navController)
            if (testID != null) {
                CameraScreen(cameraViewModel, testID, navController)
            }
        }
        composable("videoPlayer/{videoUrl}", listOf( navArgument("videoUrl") { type = NavType.StringType})){
            Log.d("AppNav","dentro video player route")
            val videoUrl = it.arguments?.getString("videoUrl")

            if (videoUrl != null) {
                VideoPlayerScreen(videoUrl)
            }
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