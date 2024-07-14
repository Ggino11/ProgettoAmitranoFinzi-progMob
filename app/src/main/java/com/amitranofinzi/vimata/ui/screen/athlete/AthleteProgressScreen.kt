package com.amitranofinzi.vimata.ui.screen.athlete

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.model.TestSet
import com.amitranofinzi.vimata.ui.theme.MessageColor
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.viewmodel.AthleteViewModel
import com.amitranofinzi.vimata.viewmodel.AuthViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthleteProgressScreen(
    athleteViewModel: AthleteViewModel = AthleteViewModel(),
    authViewModel: AuthViewModel = AuthViewModel(),
    navController: NavController
) {
    val testSets: List<TestSet> by athleteViewModel.testSets.observeAsState(emptyList())

    val athleteID = authViewModel.getCurrentUserID()

    Log.d("trainer", athleteID)
    LaunchedEffect(athleteID) {
        athleteViewModel.fetchTestSets(athleteID)
    }

    Log.d("progressScreen", testSets.toString())
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "PROGRESS SCREEN",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Primary),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            )

        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(testSets) { testSet ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("testSetDetails/${testSet.id}")
                                }
                                .shadow(8.dp, RoundedCornerShape(16.dp))
                                .background(Color.White),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = testSet.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Navigate",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(MessageColor, CircleShape)
                                        .padding(4.dp)

                                )
                            }
                        }
                    }
                }
            }

        })
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewHomeTrainerScreen() {
    VimataTheme {
        TrainerHomeScreen()
    }
}
*/