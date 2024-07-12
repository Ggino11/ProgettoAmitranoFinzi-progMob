package com.amitranofinzi.vimata.ui.screen.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@Composable
fun ExerciseEditorScreen(
    trainerViewModel: TrainerViewModel = TrainerViewModel(),
    trainerID: String,
    navController: NavController
) {
    var exerciseName by remember { mutableStateOf("") }
    var exerciseDescription by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf<Boolean?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = exerciseName,
            onValueChange = { exerciseName = it },
            label = { Text("Exercise Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = exerciseDescription,
            onValueChange = { exerciseDescription = it },
            label = { Text("Exercise Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                //firebase add exercise function
                val exercise = Exercise(
                    name= exerciseName,
                    description = exerciseDescription,
                    trainerID = trainerID,
                    collectionID = ""
                )
                trainerViewModel.addExerciseToCollection(exercise)
                trainerViewModel.addSelectedExercise(exercise)
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Crea e aggiungi esercizio")
        }

        isSuccess?.let {
            Text(text = if (it) "Exercise created successfully" else "Failed to create exercise")
        }
    }
}


