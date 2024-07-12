package com.amitranofinzi.vimata.ui.screen.viewer

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.ui.components.cards.WorkoutCard
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@Composable
fun AthleteHandlerViewerScreen(
    trainerViewModel: TrainerViewModel = TrainerViewModel(),
    authViewModel: AuthViewModel = AuthViewModel(),
    athleteID: String?,
    navController: NavController
    ) {

    val athlete by authViewModel.user.observeAsState()
    val testSets by trainerViewModel.testSets.observeAsState(emptyList())
    val workouts by trainerViewModel.workouts.observeAsState(emptyList())

    val trainerID = authViewModel.getCurrentUserID()

    LaunchedEffect(athleteID, trainerID) {
        if (athleteID != null) {
            authViewModel.fetchUser(athleteID)
            trainerViewModel.fetchTestSets(athleteID)
            trainerViewModel.fetchWorkouts(athleteID, trainerID)
        }
    }


    Log.d("AthleteViewer", athlete.toString())
    Log.d("AthleteViewer", testSets.size.toString())
    //Log.d("AthleteViewer", workouts.size.toString())

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {


        athlete?.let {
            Text(
                text = "${it.name} ${it.surname}" ,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress section
        Text(
            text = "Progress",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                IconButton(
                    onClick = {
                                navController.navigate("testSetEditor/${athleteID}")
                              },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Test Set")
                }
            }
            items(testSets) { testSet ->
                //TestSetCard(testSet = testSet, navController = navController)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("testSetDetails/${testSet.id}")
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = testSet.title, style = MaterialTheme.typography.bodyMedium)
                    }
                }


            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Workouts section
        Text(
            text = "Workouts",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                IconButton(
                    onClick = { /* Navigate to add new TestSet screen */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Workout")
                }
            }
            items(workouts) { workout ->
                WorkoutCard(workout = workout)
            }
        }


    }
}