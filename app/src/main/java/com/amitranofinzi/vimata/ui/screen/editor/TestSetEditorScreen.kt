package com.amitranofinzi.vimata.ui.screen.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.LightColor
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestSetEditorScreen(
    authViewModel: AuthViewModel,
    trainerViewModel: TrainerViewModel,
    athleteID: String,
    navController: NavController
) {
    var testSetTitle by remember { mutableStateOf("") }
    val selectedExercises by trainerViewModel.selectedExercises.observeAsState(emptyList())

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .wrapContentHeight(),
                color = BgColor,
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Primary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Edit Test Set",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            ),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                // Handle TestSet creation and save to Firestore
                                trainerViewModel.createTestSetAndTests(
                                    testSetTitle,
                                    authViewModel.getCurrentUserID(),
                                    athleteID,
                                    selectedExercises
                                )
                                navController.popBackStack()
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text("Crea Test Set")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = testSetTitle,
                        onValueChange = { testSetTitle = it },
                        label = { Text("TestSet Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = LightColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Show selected exercises in cards
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(selectedExercises) { exercise ->
                            ExerciseCard(exercise = exercise)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons to add exercises
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        //search existing exercise
                        Button(
                            onClick = {
                                navController.navigate("exerciseSelection/${authViewModel.getCurrentUserID()}")
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Search")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        //create exercise
                        Button(
                            onClick = {
                                navController.navigate("exerciseEditor/${authViewModel.getCurrentUserID()}")
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Create")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTestSetEditorScreen() {
    TestSetEditorScreen(
        authViewModel = AuthViewModel(),
        trainerViewModel = TrainerViewModel(),
        athleteID = "athleteID",
        navController = rememberNavController()
    )
}
