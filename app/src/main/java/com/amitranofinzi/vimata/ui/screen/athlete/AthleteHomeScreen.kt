package com.amitranofinzi.vimata.ui.screen.athlete

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.components.cards.TrainerCard
import com.amitranofinzi.vimata.ui.components.cards.WorkoutCard
import com.amitranofinzi.vimata.ui.components.dialog.AddTrainerDialog
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel

@Composable
fun AthleteHomeScreen(
    athleteViewModel: AthleteViewModel = AthleteViewModel(),
    authViewModel: AuthViewModel = AuthViewModel(),
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }
    val workouts by athleteViewModel.workouts.observeAsState(emptyList())
    val trainers by athleteViewModel.trainers.observeAsState(emptyList())
    val athleteID = authViewModel.getCurrentUserID()
    Log.d("homeAthlete", trainers.isEmpty().toString())

    LaunchedEffect(athleteID) {
        athleteViewModel.getTrainersForAthletes(athleteID)
        athleteViewModel.fetchWorkouts(athleteID)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top bar-like row with floating action button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(Primary)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ALLENATORI",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color.White,
                contentColor = Primary,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = "Add Trainer", tint = Primary)
            }
        }

        // Trainers section
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trainers) { trainer ->
                TrainerCard(trainer = trainer)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Header for workouts section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Primary)
                .clip(RoundedCornerShape(20.dp))
                .height(56.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {

                Icon(
                    Icons.Filled.Description,
                    contentDescription = "Icona schede",
                    tint = Color.White,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Text(
                    text = "LE MIE SCHEDE",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Workouts section
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(workouts) { workout ->
                WorkoutCard(
                    workout = workout,
                    onClick = { }
                )
            }
        }
    }

    // Show Dialog
    if (showDialog) {
        AddTrainerDialog(
            onDismiss = { showDialog = false },
            onConfirm = { email ->
                athleteViewModel.addTrainerAndChat(email, athleteID)
                showDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAthleteHomeScreen() {
    VimataTheme {
        val athleteViewModel: AthleteViewModel = AthleteViewModel()
        val authViewModel: AuthViewModel = AuthViewModel()
        val navController = rememberNavController()
        AthleteHomeScreen(athleteViewModel, authViewModel, navController)
    }
}
