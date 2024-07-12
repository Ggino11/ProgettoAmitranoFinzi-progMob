package com.amitranofinzi.vimata.ui.screen.viewer

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.extensions.TestStatus
import com.amitranofinzi.vimata.ui.components.cards.TestCard
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.CameraViewModel
import java.net.URLEncoder

@Composable
fun TestSetViewerScreen(
    authViewModel: AuthViewModel = AuthViewModel(),
    athleteViewModel: AthleteViewModel = AthleteViewModel(),
    cameraViewModel: CameraViewModel = CameraViewModel(),
    testSetId: String?,
    navController: NavController
    ) {

    val tests by athleteViewModel.tests.observeAsState(emptyList())
    val user by authViewModel.user.observeAsState()

    val userID = authViewModel.getCurrentUserID()

    LaunchedEffect(userID) {
        authViewModel.fetchUser(userID)
    }

    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d("permissionLaunch", "update isGranted ${isGranted}")
        cameraViewModel.updatePermissionStatus(isGranted)
        if (isGranted) {
            Log.d("permissionLaunch", "permission isGranted ${isGranted}")
        } else {
            Log.d("permissionLaunch", "permission isGranted ${isGranted}")
            Toast.makeText(context, "Camera permission is required to record video", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(testSetId) {
        athleteViewModel.fetchTests(testSetId)
    }
    Log.d("TestSetViewer", tests.isEmpty().toString())
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        LazyColumn {
            Log.d("TestSetViewer", tests.toString())
            items(tests) { test ->
                Log.d("TestSetViewer", test.toString())

                TestCard(
                    userType = user!!.userType,
                    test = test,
                    onVideoClick = {
                        cameraViewModel.checkCameraPermission { isGranted ->
                            if (isGranted) {
                                navController.navigate("cameraScreen/${test.id}")
                            } else {
                                cameraViewModel.requestCameraPermission()
                                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                Log.d("TestCard", "permission isGranted ${isGranted}")
                                if(isGranted) { navController.navigate("cameraScreen/${test.id}")}
                                athleteViewModel.fetchTests(testSetId)

                            }
                        }

                        Log.d("TestCard", "Video recording for ${test.exerciseName}")
                    },
                    onResultEntered = {result ->
                        // Implement result input logic
                        Log.d("TestCard", "Input result for ${test.exerciseName} is ${result.toString()}")
                        athleteViewModel.updateTestResult(test.copy(result = result))
                        athleteViewModel.fetchTests(testSetId)

                    },
                    onPlayVideoClick = {
                        if(test.videoUrl!="") {
                            Log.d("TestCard", "Video url ${test.videoUrl}")
                            val encodedUrl = URLEncoder.encode(test.videoUrl, "UTF-8")
                            navController.navigate("videoPlayer/${encodedUrl}")
                        }
                        else Log.d("TestCard", "Video url is empty")
                    },
                    onConfirmClick = {
                        // Implement result confirmation logic
                        // Change test state updating firebase
                        if(test.status == TestStatus.TODO) {
                            athleteViewModel.updateTestStatus(test.copy(status = TestStatus.DONE))
                        }
                        else if(test.status == TestStatus.DONE) {
                            athleteViewModel.updateTestStatus(test.copy(status = TestStatus.VERIFIED))
                        }
                        athleteViewModel.fetchTests(testSetId)

                        Log.d("TestCard", "Confirm result for ${test.exerciseName}")
                    }
                )
            }
        }
    }

}