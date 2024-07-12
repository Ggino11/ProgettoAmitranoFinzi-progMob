package com.amitranofinzi.vimata.ui.screen.athlete

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.components.TrainerCard
import com.amitranofinzi.vimata.ui.components.cards.WorkoutCard
import com.amitranofinzi.vimata.ui.components.dialog.AddTrainerDialog
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel

@Composable
fun AthleteHomeScreen(athleteViewModel: AthleteViewModel = AthleteViewModel(),
                      authViewModel: AuthViewModel = AuthViewModel(),
                      navController: NavController
) {

//    val workouts = dummyWorkouts
    var showDialog by remember { mutableStateOf(false) }
    val workouts by athleteViewModel.workouts.observeAsState(emptyList())
    val trainers by athleteViewModel.trainers.observeAsState(emptyList())
    val athleteID = authViewModel.getCurrentUserID()
    Log.d("homeAthlete", trainers.isEmpty().toString())

    LaunchedEffect(athleteID) {
        athleteViewModel.getTrainersForAthletes(athleteID)
        athleteViewModel.fetchWorkouts(athleteID)
    }


    Column(modifier = Modifier.fillMaxSize()) {
        // Column for trainers
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ALLENATORI",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Filled.PersonAdd, contentDescription = "Add Trainer")
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(trainers) { trainer ->
                    TrainerCard(trainer = trainer,)
                }
            }
        }

        // Column for workouts
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f)) {
            Text(
                text = "LE MIE SCHEDE",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            LazyColumn( modifier = Modifier.fillMaxHeight(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(workouts) { workout ->
                    WorkoutCard(
                        workout = workout,
                        onClick = { }
                    )
                }
            }
        }
    }
    // Show Dialog
    //TODO: funciton to remove
    if (showDialog) {
        AddTrainerDialog(
            onDismiss = { showDialog = false },
            onConfirm = {email ->
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
        val alathleteViewModel: AthleteViewModel = AthleteViewModel()
        val authViewModel: AuthViewModel = AuthViewModel()
        val navController = rememberNavController()
        AthleteHomeScreen(alathleteViewModel,authViewModel,navController)
        }


   }

