package com.amitranofinzi.vimata.ui.screen.viewer

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@Composable
fun CollectionViewerScreen(
    trainerViewModel: TrainerViewModel = TrainerViewModel(),
    collectionID: String?,
    navController: NavController
    ) {

    val exercises by trainerViewModel.exercises.observeAsState(emptyList())

    val collection by trainerViewModel.collection.observeAsState()

    LaunchedEffect(collectionID) {
        trainerViewModel.fetchExercises(collectionID)
        if (collectionID != null) {
            trainerViewModel.fetchCollection(collectionID)
        }
    }
    Log.d("CollectionViewer", exercises.isEmpty().toString())
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {


        collection?.let {
            Text(
                text = it.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start)
            )
        }

        LazyColumn {
            items(exercises) { exercise ->
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
                        Text(text = exercise.name , style = MaterialTheme.typography.bodyLarge)
                        Text(text = exercise.description,  style = MaterialTheme.typography.bodyMedium)
                        IconButton(onClick = { /* Play video */ }) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play Video")
                        }
                    }
                }
            }
        }
    }
}