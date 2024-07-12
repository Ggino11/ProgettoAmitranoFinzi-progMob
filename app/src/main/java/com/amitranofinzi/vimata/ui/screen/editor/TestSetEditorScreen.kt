package com.amitranofinzi.vimata.ui.screen.viewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.ui.components.cards.ExerciseCard
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@Composable
fun TestSetEditorScreen(
    authViewModel: AuthViewModel,
    trainerViewModel: TrainerViewModel,
    athleteID: String,
    navController: NavController
) {
    var testSetTitle by remember { mutableStateOf("") }
    val selectedExercises by trainerViewModel.selectedExercises.observeAsState(emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = testSetTitle,
            onValueChange = { testSetTitle = it },
            label = { Text("TestSet Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show selected exercises in cards
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(selectedExercises) { exercise ->
                ExerciseCard(exercise = exercise)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons to add exercises
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    navController.navigate("exerciseSelection/${authViewModel.getCurrentUserID()}")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cerca Esercizio")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    navController.navigate("exerciseEditor/${authViewModel.getCurrentUserID()}")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Crea Esercizio")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button to save TestSet
        Button(
            onClick = {
                // Handle TestSet creation and save to Firestore

                trainerViewModel.createTestSetAndTests(testSetTitle, authViewModel.getCurrentUserID(), athleteID, selectedExercises)
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Crea Test Set")
        }
    }
}



