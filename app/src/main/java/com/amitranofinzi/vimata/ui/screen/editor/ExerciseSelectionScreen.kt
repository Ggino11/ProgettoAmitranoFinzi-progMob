package com.amitranofinzi.vimata.ui.screen.editor

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@Composable
fun ExerciseSelectionScreen(
    trainerViewModel: TrainerViewModel,
    trainerID: String,
    navController: NavController
) {
    val exercises by trainerViewModel.exercises.observeAsState(emptyList())
    val selectedExercises by trainerViewModel.selectedExercises.observeAsState(emptyList())
    val selectedExerciseIds = remember { mutableStateMapOf<String, Boolean>().apply {
        selectedExercises.forEach { put(it.id, true) }
    } }

    LaunchedEffect(trainerID) {
        trainerViewModel.fetchExercisesByTrainerId(trainerID)
    }

    Log.d("exerciseSelection", exercises.toString())

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (list, button) = createRefs()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(parent.top)
                    bottom.linkTo(button.top)
                    height = Dimension.fillToConstraints
                }
                .padding(bottom = 16.dp)
        ) {
            items(exercises) { exercise ->
                val isSelected = selectedExerciseIds[exercise.id] ?: false
                ExerciseSelectionItem(
                    exercise = exercise,
                    isSelected = isSelected,
                    onExerciseSelected = {
                        selectedExerciseIds[exercise.id] = !isSelected
                    }
                )
            }
        }

        Button(
            onClick = {
                val selectedExercises = exercises.filter { selectedExerciseIds[it.id] == true }
                trainerViewModel.setSelectedExercises(selectedExercises)
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(16.dp)
                .zIndex(1f)
        ) {
            Text("Conferma Selezione")
        }
    }
}

@Composable
fun ExerciseSelectionItem(
    exercise: Exercise,
    isSelected: Boolean,
    onExerciseSelected: (Exercise) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clickable { onExerciseSelected(exercise) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onExerciseSelected(exercise) },
                modifier = Modifier.size(24.dp)
            )
        }
    }
}