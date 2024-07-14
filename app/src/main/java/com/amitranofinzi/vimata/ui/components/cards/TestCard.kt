package com.amitranofinzi.vimata.ui.components.cards

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.extensions.TestStatus
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.ui.components.dialog.InputResultDialog
import com.amitranofinzi.vimata.ui.theme.VerifiedColor
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import java.util.Locale

@Composable
fun TestCard(
    userType: String,
    test: Test,
    onVideoClick: () -> Unit,
    onResultEntered: (Double) -> Unit,
    onPlayVideoClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    val testStatus = test.status
    val borderColor = when (testStatus) {
        TestStatus.TODO -> MaterialTheme.colorScheme.error
        TestStatus.DONE -> MaterialTheme.colorScheme.secondary
        TestStatus.VERIFIED -> VerifiedColor
    }
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(borderColor, borderColor.copy(alpha = 0.5f))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .background(color = Color.White)
            .clickable { /* Handle card click if needed */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp) // Angoli arrotondati
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
                        FloatingIconButton(
                            onClick = onVideoClick,
                            icon = Icons.Default.Videocam,
                            description = "Record Video",
                            borderColor = MaterialTheme.colorScheme.error
                        )
                        FloatingIconButton(
                            onClick = onPlayVideoClick,
                            icon = Icons.Default.PlayArrow,
                            description = "Play Video",
                            borderColor = MaterialTheme.colorScheme.error
                        )
                        FloatingIconButton(
                            onClick = { showDialog = true },
                            icon = Icons.Default.Edit,
                            description = "Input Result",
                            borderColor = MaterialTheme.colorScheme.error
                        )
                        FloatingIconButton(
                            onClick = onConfirmClick,
                            icon = Icons.Default.Check,
                            description = "Confirm Result",
                            borderColor = MaterialTheme.colorScheme.error
                        )
                    }
                    TestStatus.DONE -> {
                        FloatingIconButton(
                            onClick = onVideoClick,
                            icon = Icons.Default.Videocam,
                            description = "Record Video",
                            borderColor = MaterialTheme.colorScheme.secondary
                        )
                        FloatingIconButton(
                            onClick = onPlayVideoClick,
                            icon = Icons.Default.PlayArrow,
                            description = "Play Video",
                            borderColor = MaterialTheme.colorScheme.secondary
                        )
                        FloatingIconButton(
                            onClick = { showDialog = true },
                            icon = Icons.Default.Edit,
                            description = "Input Result",
                            borderColor = MaterialTheme.colorScheme.secondary
                        )
                        if (userType == "trainer") {
                            FloatingIconButton(
                                onClick = onConfirmClick,
                                icon = Icons.Default.Check,
                                description = "Confirm Result",
                                borderColor = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    TestStatus.VERIFIED -> {
                        FloatingIconButton(
                            onClick = onPlayVideoClick,
                            icon = Icons.Default.PlayArrow,
                            description = "Play Video",
                            borderColor = VerifiedColor
                        )
                    }
                }
            }
            Text(
                text = "${test.comment}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    if (showDialog) {
        InputResultDialog(
            onResultEntered = { result ->
                Log.d("TestCard", "Input result for ${test.exerciseName} is ${result.toString()}")
                onResultEntered(result)
                showDialog = false
            },
            onDismissRequest = { showDialog = false })
    }
}

@Composable
fun FloatingIconButton(onClick: () -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector, description: String, borderColor: Color) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .border(1.dp, borderColor, CircleShape)
            .shadow(4.dp, CircleShape) // Effetto ombra
            .background(color = Color.White, shape = CircleShape) // Sfondo bianco
    ) {
        Icon(icon, contentDescription = description)
    }
}

@Composable
@Preview
fun PreviewTestCard() {
    val test = Test(
        id = "1",
        exerciseName = "Push-up",
        result = 20.0,
        unit = "reps",
        status = TestStatus.TODO,
        comment = "Great form, keep it up!"
    )

    VimataTheme {
        TestCard(
            userType = "athlete",
            test = test,
            onVideoClick = { Log.d("Preview", "Record video clicked") },
            onResultEntered = { result -> Log.d("Preview", "Result entered: $result") },
            onPlayVideoClick = { Log.d("Preview", "Play video clicked") },
            onConfirmClick = { Log.d("Preview", "Confirm clicked") }
        )
    }
}
