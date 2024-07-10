package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.User

@Composable
fun AthleteCard(athlete: User, modifier: Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${athlete.name} ${athlete.surname}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Email: ${athlete.email}", style = MaterialTheme.typography.bodySmall)
            // Add other fields as needed
        }
    }
}