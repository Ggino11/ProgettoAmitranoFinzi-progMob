package com.amitranofinzi.vimata.ui.screen.athlete

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.model.Workout
import com.amitranofinzi.vimata.ui.components.WorkoutCard
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@Composable
fun AthleteHomeScreen(athleteViewModel: AthleteViewModel = AthleteViewModel(),
                      authViewModel: AuthViewModel = AuthViewModel(),
                      navController: NavController
) {

    val workouts by athleteViewModel.workouts.observeAsState(emptyList())

    val athleteID = authViewModel.getCurrentUserID()

    LaunchedEffect(athleteID){
        athleteViewModel.fetchWorkouts(athleteID)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "LE MIE SCHEDE",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Start)
        )

        LazyColumn {
            items(workouts) { workout ->
                WorkoutCard(
                    workout = workout,
                    onClick = { }
                )
            }
        }
    }
}




/*

@Preview(showBackground = true)
@Composable
fun PreviewAthleteHomeScreen() {
    VimataTheme {
        AthleteHomeScreen()
    }
}
*/