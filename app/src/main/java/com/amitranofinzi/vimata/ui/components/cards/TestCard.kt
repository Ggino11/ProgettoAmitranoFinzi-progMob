package com.amitranofinzi.vimata.ui.components.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.extensions.TestStatus
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.ui.theme.VerifiedColor
import java.util.Locale

@Composable
fun TestCard(
    test: Test,
    onVideoClick: () -> Unit,
    onResultClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    val testStatus = test.status
    val borderColor = when (testStatus) {
        TestStatus.TODO -> MaterialTheme.colorScheme.primary
        TestStatus.DONE -> MaterialTheme.colorScheme.secondary
        TestStatus.VERIFIED -> VerifiedColor
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(2.dp, borderColor)
            .clickable { /* Handle card click if needed */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = test.status?.toString()
                    ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    ?: "Unknown",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp)
                    .align(Alignment.Start)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = test.exerciseName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Result: ${test.result} ${test.unit}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Render buttons based on testStatus
                when (testStatus) {

                    TestStatus.TODO -> {
                        IconButton(onClick = onVideoClick, modifier= Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)) {
                            Icon(Icons.Default.Videocam, contentDescription = "Record Video")
                        }
                        // if video is recorded you can play it
                        IconButton(onClick = { /* Play video */ },  modifier= Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play Video")
                        }
                        // Write the result number
                        IconButton(onClick = onResultClick,  modifier= Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)) {
                            Icon(Icons.Default.Edit, contentDescription = "Input Result")
                        }

                        // If result is written this confirm the result and change state to DONE
                        IconButton(onClick = onConfirmClick,  modifier= Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)) {
                            Icon(Icons.Default.Check, contentDescription = "Confirm Result")
                        }

                    }
                    TestStatus.DONE -> {

                        IconButton(onClick = onVideoClick,  modifier= Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)) {
                            Icon(Icons.Default.Videocam, contentDescription = "Record Video")
                        }
                        IconButton(onClick = { /* Play video */ },  modifier= Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play Video")
                        }
                        IconButton(onClick = onResultClick,  modifier= Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)) {
                            Icon(Icons.Default.Edit, contentDescription = "Input Result")
                        }

                    }

                    TestStatus.VERIFIED -> {
                        IconButton(onClick = { /* Play video */ },  modifier= Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play Video")
                        }

                    }
                }
            }
            Text(
                text = "${test.comment}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

