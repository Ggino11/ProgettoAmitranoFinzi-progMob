package com.amitranofinzi.vimata.ui.screen.trainer

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainerWorkbookScreen(
    trainerViewModel: TrainerViewModel = TrainerViewModel(),
    authViewModel: AuthViewModel = AuthViewModel(),
    navController: NavController
) {
    val collections by trainerViewModel.collections.observeAsState(emptyList())


    val trainerID = authViewModel.getCurrentUserID()

    LaunchedEffect(trainerID) {
        trainerViewModel.fetchCollections(trainerID)
    }
    Log.d("TrainerWorkbookScreen", collections.toString())


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Show AddCollectionDialog */ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Collection")
            }
        }
    ){
        Column {
            Text(
                text = "Workbook",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start)
            )

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(collections) { collection ->
                    Log.d("TrainerWorkbookScreen", collection.id)
                    Log.d("TrainerWorkbookScreen", collection.title)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {

                                navController.navigate("collection/${collection.id}")
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = collection.title, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}





