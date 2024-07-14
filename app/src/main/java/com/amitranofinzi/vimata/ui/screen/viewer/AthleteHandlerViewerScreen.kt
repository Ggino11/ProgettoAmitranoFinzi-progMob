package com.amitranofinzi.vimata.ui.screen.viewer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.ui.components.ProfileAvatar
import com.amitranofinzi.vimata.ui.components.cards.WorkoutCard
import com.amitranofinzi.vimata.ui.theme.MessageColor
import com.amitranofinzi.vimata.ui.theme.Secondary
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(userName = athlete?.name, userLastName = athlete?.surname)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${athlete?.name} ${athlete?.surname}",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Back")
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PROGRESS",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButton(
                onClick = { navController.navigate("testSetEditor/${athleteID}") },
                containerColor = Secondary,
                contentColor = Color.White,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Test Set",
                    tint = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            items(testSets) { testSet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("testSetDetails/${testSet.id}")
                        }
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .background(Color.White),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = testSet.title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Navigate",
                            modifier = Modifier
                                .size(24.dp)
                                .background(MessageColor, CircleShape)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Text(
                text = "WORKOUTS",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButton(
                onClick = { /* Navigate to add new Workout screen */ },
                containerColor = Secondary,
                contentColor = Color.White,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Workout",
                    tint = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            items(workouts) { workout ->
                WorkoutCard(workout = workout)
            }
        }
    }
}

