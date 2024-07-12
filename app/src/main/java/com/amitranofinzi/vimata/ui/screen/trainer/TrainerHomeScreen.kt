package com.amitranofinzi.vimata.ui.screen.trainer

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.ui.components.cards.AthleteCard
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@Composable
fun TrainerHomeScreen(
    trainerViewModel: TrainerViewModel = TrainerViewModel(),
    authViewModel: AuthViewModel = AuthViewModel(),
    navController: NavController
) {
    val athletes: List<User> by trainerViewModel.athletes.observeAsState(emptyList())

    val trainerID = authViewModel.getCurrentUserID()

    Log.d("trainer",trainerID)
    LaunchedEffect(trainerID) {
        trainerViewModel.getAthletesForCoach(trainerID)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Your Athletes",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Start)
        )

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(athletes) { athlete ->
                AthleteCard(athlete = athlete,
                            modifier = Modifier.fillMaxWidth()
                                                .padding(vertical = 8.dp)
                                                .clickable {

                                                    navController.navigate("athleteHandler/${athlete.uid}")
                                                }
                )
            }
        }

    }
}
/*
@Preview(showBackground = true)
@Composable
fun PreviewHomeTrainerScreen() {
    VimataTheme {
        TrainerHomeScreen()
    }
}
*/