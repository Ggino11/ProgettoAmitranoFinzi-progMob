package com.amitranofinzi.vimata.ui.screen.viewer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.ui.theme.GrayColor
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.VimataTheme

@Composable
fun ExerciseCard(exercise: Exercise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .border(BorderStroke(1.dp, Primary)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = GrayColor
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewExerciseCard() {
    VimataTheme {
    ExerciseCard(
        exercise = Exercise(
            name = "Push-Up",
            description = "A bodyweight exercise for upper body strength."
        )
    )}
}
