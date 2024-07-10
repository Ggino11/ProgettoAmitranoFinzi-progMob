package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.Workout
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.ui.theme.VimataTheme

@Composable
fun WorkoutCard(modifier: Modifier = Modifier, workout: Workout, onClick: () -> Unit = {}) {

    Card(
        elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(Secondary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = workout.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Status: ${workout.status}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Trainer: ${workout.trainerID}", style = MaterialTheme.typography.bodySmall)
        }
    }

}
@Composable
@Preview
fun preview (){
    VimataTheme {
        val workout = Workout()
        WorkoutCard(workout =workout) {

        }
    }
}