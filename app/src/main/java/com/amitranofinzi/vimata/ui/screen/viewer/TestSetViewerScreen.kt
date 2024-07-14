package com.amitranofinzi.vimata.ui.screen.viewer

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.extensions.TestStatus
import com.amitranofinzi.vimata.ui.components.cards.TestCard
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.Secondary
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
        .padding(bottom= 16.dp)) {

        // Top bar
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            border = BorderStroke(1.dp, Secondary)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "TEST VIEWER",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Primary, // Colore gradiente iniziale
                                Secondary  // Colore gradiente finale
                            )
                        )
                    )
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        }
        LazyColumn (
            modifier = Modifier.padding(horizontal=16.dp)
        ){
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