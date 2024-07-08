package com.amitranofinzi.vimata.ui.screen.viewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.viewmodel.TestViewModel

@Composable
fun TestSetViewerScreen(
    testSetId: String?,
    navController: NavController
    ) {

    val testViewModel = TestViewModel()
    val tests by testViewModel.tests.observeAsState(emptyList())

    LaunchedEffect(testSetId) {
        testViewModel.fetchTests(testSetId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn {
            items(tests) { test ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            // Open dialog with test.comment
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = test.exerciseName, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Result: ${test.result} ${test.unit}",  style = MaterialTheme.typography.bodyMedium)
                        IconButton(onClick = { /* Play video */ }) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play Video")
                        }
                    }
                }
            }
        }
    }
}