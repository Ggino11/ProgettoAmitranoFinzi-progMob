package com.amitranofinzi.vimata.ui.screen.athlete

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.Workout
import com.amitranofinzi.vimata.ui.components.WorkoutCard
import com.amitranofinzi.vimata.ui.theme.VimataTheme

@Composable
fun AthleteHomeScreen() {
    val workout = Workout(
        "00",
        "Summer workout",
        "active",
        "Coach Frank",
        "www.pdfurl.com")

    val workout1 = Workout(
        "00",
        "Basket workout",
        "active",
        "Coach Frank",
        "www.pdfurl.com")

    val workout2 = Workout(
        "00",
        "Tennis workout",
        "active",
        "Coach Frank",
        "www.pdfurl.com")
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "HOME",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Start)
        )

        WorkoutCard(workout = workout, onClick = { /*onWorkoutCardClick(sheet.pdfUrl)*/ })
        WorkoutCard(workout = workout1, onClick = { /*onWorkoutCardClick(sheet.pdfUrl)*/ })
        WorkoutCard(workout = workout2, onClick = { /*onWorkoutCardClick(sheet.pdfUrl)*/ })

    }





}

@Preview(showBackground = true)
@Composable
fun PreviewAthleteHomeScreen() {
    VimataTheme {
        AthleteHomeScreen()
    }
}