package com.amitranofinzi.vimata.ui.screen.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.ui.theme.LightColor
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseEditorScreen(
    trainerViewModel: TrainerViewModel = TrainerViewModel(),
    trainerID: String,
    navController: NavController
) {
    var exerciseName by remember { mutableStateOf("") }
    var exerciseDescription by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf<Boolean?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary) // Imposta il colore blu dello sfondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
                .padding(16.dp)
                .border(2.dp, Primary, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "Create Exercise",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp)
            )

            TextField(
                value = exerciseName,
                onValueChange = { exerciseName = it },
                label = { Text("Exercise Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = LightColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = exerciseDescription,
                onValueChange = { exerciseDescription = it },
                label = { Text("Exercise Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = LightColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val exercise = Exercise(
                            name = exerciseName,
                            description = exerciseDescription,
                            trainerID = trainerID,
                            collectionID = ""
                        )
                        trainerViewModel.addExerciseToCollection(exercise)
                        trainerViewModel.addSelectedExercise(exercise)
                        navController.popBackStack()
                    }
                ) {
                    Text("Create and add exercise")
                }
            }

            isSuccess?.let {
                Text(
                    text = if (it) "Exercise created successfully" else "Failed to create exercise",
                    color = if (it) Color.Green else Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
                )
            }
        }
    }
}

