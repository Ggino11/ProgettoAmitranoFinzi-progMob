package com.amitranofinzi.vimata.ui.screen.athlete

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.amitranofinzi.vimata.viewmodel.TestViewModel
import android.util.Log
import com.amitranofinzi.vimata.data.model.TestSet


@Composable
fun AthleteProgressScreen(
    testViewModel: TestViewModel = TestViewModel(),
    athleteID: String,
    navController: NavController
) {
    val testSets: List<TestSet> by testViewModel.testSets.observeAsState(emptyList())

    LaunchedEffect(athleteID) {
        testViewModel.fetchTestSets(athleteID)
    }

    Log.d("progressScreen", testSets[0].toString() )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "I MIEI PROGRESSI",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Start)
        )
        LazyColumn {
            items(testSets) { testSet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("testSetDetails/${testSet.id}")
                        },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = testSet.title, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

        }
    }
}




/*

@Preview(showBackground = true)
@Composable
fun PreviewAthleteProgressScreen() {
    val testViewModel = TestViewModel()
    val athleteID = "111"
    //val navController =  rememberNavController()
    VimataTheme {
        AthleteProgressScreen(testViewModel,athleteID)
    }
}
*/