package com.amitranofinzi.vimata.ui.navigation


import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.amitranofinzi.vimata.data.database.AppDatabase
import com.amitranofinzi.vimata.ui.screen.athlete.AthleteScreen
import com.amitranofinzi.vimata.ui.screen.auth.LoginScreen
import com.amitranofinzi.vimata.ui.screen.chat.SingleChatScreen
import com.amitranofinzi.vimata.ui.screen.editor.ExerciseEditorScreen
import com.amitranofinzi.vimata.ui.screen.editor.ExerciseSelectionScreen
import com.amitranofinzi.vimata.ui.screen.editor.WorkoutEditorScreen
import com.amitranofinzi.vimata.ui.screen.sensing.VideoPlayerScreen
import com.amitranofinzi.vimata.ui.screen.trainer.TrainerScreen
import com.amitranofinzi.vimata.ui.screen.viewer.AthleteHandlerViewerScreen
import com.amitranofinzi.vimata.ui.screen.viewer.CollectionViewerScreen
import com.amitranofinzi.vimata.ui.screen.viewer.TestSetEditorScreen
import com.amitranofinzi.vimata.ui.screen.viewer.TestSetViewerScreen
import com.amitranofinzi.vimata.ui.screens.SignUpScreen
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.CameraViewModel
import com.amitranofinzi.vimata.viewmodel.ChatViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel
import com.example.yourapp.CameraScreen
import java.net.URLDecoder

@Composable
fun AppNav(
    appDatabase: AppDatabase,
    context: Context
){

    val navController = rememberNavController()

    // NESTED GRAPH CONSTRUCTOR
    // Vengono in questa funzione tutti i grafi di navigazione principali
    // definiti ognuno tramite una funzione dedicata
    // Per ognuno di essi viene definito uno shared viewModel
    NavHost(navController = navController, startDestination = "auth") {
        authGraph(navController, appDatabase, context)
        athleteGraph(navController, appDatabase, context)
        trainerGraph(navController, appDatabase, context)

    }

}
fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    appDatabase: AppDatabase,
    context: Context) {
    navigation( startDestination = "login", route = "auth"){

        composable("login"){
            Log.d("login", "Login prima dello shared view model")
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)
            Log.d("login", "Login dopo lo shared view model")
            LoginScreen(authViewModel = authViewModel, navController = navController);
        }
        composable("signup"){
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)
            SignUpScreen(authViewModel = authViewModel, navController = navController);
        }
        composable("forgot_psw"){
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)
        }
    }
}
fun NavGraphBuilder.athleteGraph(
    navController: NavHostController,
    appDatabase: AppDatabase,
    context: Context) {

    navigation(
        startDestination = "athlete_screen",
        route = "athlete"
    ){
        composable("athlete_screen"){
            val athleteViewModel = it.sharedViewModel<AthleteViewModel>(navController, appDatabase, context)
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)
            val chatViewModel = it.sharedViewModel<ChatViewModel>(navController, appDatabase, context)
            Log.d("athlete graph", "prima di athlete screen")
            AthleteScreen(athleteViewModel, authViewModel, chatViewModel, navController)
        }
        composable("testSetDetails/{testSetId}",listOf( navArgument("testSetId") { type = NavType.StringType  })
        ){
            // Ottieni l'ID del test set dalla navigazione
            val testSetId = it.arguments?.getString("testSetId")
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)
            val athleteViewModel = it.sharedViewModel<AthleteViewModel>(navController, appDatabase, context)
            val cameraViewModel = it.sharedViewModel<CameraViewModel>(navController, appDatabase, context)
            // Visualizza i dettagli del test set con l'ID specificato
            TestSetViewerScreen(authViewModel, athleteViewModel,cameraViewModel,testSetId, navController)
        }

        composable("chatDetails/{chatId}/{receiverId}",
                listOf(
                    navArgument("chatId") { type = NavType.StringType},
                    navArgument("receiverId") { type = NavType.StringType })
        ){

            val chatId = it.arguments?.getString("chatId")
            val receiverId = it.arguments?.getString("receiverId")

            Log.d("NavigationTOSingleChat",chatId!!)
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)
            val chatViewModel = it.sharedViewModel<ChatViewModel>(navController, appDatabase, context)
            if (chatId != null) {
                Log.d("NavigationTOSingleChat",chatId)
            } else {
                Log.d("NavigationTOSingleChat","NUll")
            }
            SingleChatScreen(authViewModel,chatViewModel,chatId, receiverId, navController)
        }
        composable("cameraScreen/{testID}", listOf( navArgument("testID") { type = NavType.StringType})){
            Log.d("AppNav","dentro cameraScreen route")
            val testID = it.arguments?.getString("testID")

            val cameraViewModel = it.sharedViewModel<CameraViewModel>(navController, appDatabase, context)
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

fun NavGraphBuilder.trainerGraph(
    navController: NavHostController,
    appDatabase: AppDatabase,
    context: Context) {

    navigation(
        startDestination = "trainer_screen",
        route = "trainer"
    ){
        composable("trainer_screen"){
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)
            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController, appDatabase, context)
            val chatViewModel = it.sharedViewModel<ChatViewModel>(navController, appDatabase, context)
            TrainerScreen(trainerViewModel,authViewModel,chatViewModel, navController)
        }
        composable("collection/{collectionID}",listOf( navArgument("collectionID") { type = NavType.StringType  })
        ){
            // Ottieni l'ID del test set dalla navigazione
            val collectionID = it.arguments?.getString("collectionID")
            Log.d("AppNav-TrainerGraph", collectionID.isNullOrEmpty().toString())

            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController, appDatabase, context)
            // Visualizza i dettagli del test set con l'ID specificato
            CollectionViewerScreen(trainerViewModel,collectionID, navController)
        }
        composable("athleteHandler/{athleteID}",listOf( navArgument("athleteID") { type = NavType.StringType  })
        ){
            // Ottieni l'ID del test set dalla navigazione
            val athleteID = it.arguments?.getString("athleteID")
            Log.d("AppNav-TrainerGraph", athleteID.isNullOrEmpty().toString())

            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController, appDatabase, context)
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)

            // Visualizza i dettagli del test set con l'ID specificato
            AthleteHandlerViewerScreen(trainerViewModel,authViewModel,athleteID, navController)
        }
        composable("testSetEditor/{athleteID}",listOf( navArgument("athleteID") { type = NavType.StringType  })
        ){
            val athleteID = it.arguments?.getString("athleteID")
            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController, appDatabase, context)
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)

            TestSetEditorScreen(authViewModel,trainerViewModel,athleteID!!,navController)
        }
        composable("exerciseEditor/{trainerID}",listOf( navArgument("trainerID") { type = NavType.StringType  }))
        {
            val trainerID = it.arguments?.getString("trainerID")
            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController, appDatabase, context)
            if (trainerID != null) {
                ExerciseEditorScreen(trainerViewModel, trainerID, navController)
            }
        }
        composable("workoutEditor/{athleteID}",listOf( navArgument("athleteID") { type = NavType.StringType  }))
        {
            val athleteID = it.arguments?.getString("athleteID")
            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController, appDatabase, context)
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)

            if (athleteID != null) {
                WorkoutEditorScreen(trainerViewModel, authViewModel, athleteID, navController)
            }
        }
        composable("exerciseSelection/{trainerID}",listOf( navArgument("trainerID") { type = NavType.StringType  }))
        {
            val trainerID = it.arguments?.getString("trainerID")
            val trainerViewModel = it.sharedViewModel<TrainerViewModel>(navController, appDatabase, context)
            if (trainerID != null) {
                ExerciseSelectionScreen(trainerViewModel, trainerID, navController)
            }
        }

        // TestSet viewer screen
        composable("testSetDetails/{testSetId}",listOf( navArgument("testSetId") { type = NavType.StringType  })
        ){
            // Ottieni l'ID del test set dalla navigazione
            val testSetId = it.arguments?.getString("testSetId")
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)
            val athleteViewModel = it.sharedViewModel<AthleteViewModel>(navController, appDatabase, context)
            val cameraViewModel = it.sharedViewModel<CameraViewModel>(navController, appDatabase, context)
            // Visualizza i dettagli del test set con l'ID specificato
            TestSetViewerScreen(authViewModel, athleteViewModel,cameraViewModel,testSetId, navController)
        }
        composable("chatDetails/{chatId}/{receiverId}",
            listOf(
                navArgument("chatId") { type = NavType.StringType},
                navArgument("receiverId") { type = NavType.StringType })
        ){

            val chatId = it.arguments?.getString("chatId")
            val receiverId = it.arguments?.getString("receiverId")

            Log.d("NavigationTOSingleChat",chatId!!)
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController, appDatabase, context)
            val chatViewModel = it.sharedViewModel<ChatViewModel>(navController, appDatabase, context)
            if (chatId != null) {
                Log.d("NavigationTOSingleChat",chatId)
            } else {
                Log.d("NavigationTOSingleChat","NUll")
            }
            SingleChatScreen(authViewModel,chatViewModel,chatId, receiverId, navController)
        }
        composable("cameraScreen/{testID}", listOf( navArgument("testID") { type = NavType.StringType})){
            Log.d("AppNav","dentro cameraScreen route")
            val testID = it.arguments?.getString("testID")

            val cameraViewModel = it.sharedViewModel<CameraViewModel>(navController, appDatabase, context)
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
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    appDatabase: AppDatabase,
    context: Context
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    val viewModel: T = viewModel(parentEntry)

    // Inizializza il ViewModel se supporta l'inizializzazione
    if (viewModel is InitializableViewModel) {
        (viewModel as InitializableViewModel).initialize(appDatabase, context)
    }

    return viewModel
}

interface InitializableViewModel {
    fun initialize(appDatabase: AppDatabase, context: Context)
}


