package com.amitranofinzi.vimata.ui.screen.trainer

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.Secondary
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



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom= 16.dp)
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                border = BorderStroke(1.dp, Secondary)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "WORKBOOK",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                         style = MaterialTheme.typography.titleLarge.copy(
                        brush = Brush.linearGradient(
                             colors = listOf(
                                 Primary,
                                 Secondary
                        )
                    )
                ))
                }
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(collections) { collection ->
                    Log.d("TrainerWorkbookScreen", collection.id)
                    Log.d("TrainerWorkbookScreen", collection.title)

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
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = collection.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { navController.navigate("collection/${collection.id}") }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                        contentDescription = "Back",
                                        tint = Primary
                                    )
                                }

                            }
                        }

                    }
                }
            }
        }
}






