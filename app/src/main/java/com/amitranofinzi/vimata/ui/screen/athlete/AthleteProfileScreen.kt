package com.amitranofinzi.vimata.ui.screen.athlete

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.amitranofinzi.vimata.data.model.Athlete
import com.amitranofinzi.vimata.ui.theme.GrayColor
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthleteProfileScreen(athlete: Athlete, onEditProfileClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Athlete Profile") },
                actions = {
                    IconButton(onClick = onEditProfileClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(athlete)
            ProfileDetails(athlete)
        }
    }
}
@Composable
fun ProfileHeader(athlete: Athlete) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(athlete.profilePictureUrl),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .border(2.dp, GrayColor, CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = athlete.name, style = MaterialTheme.typography.headlineSmall )
        Text(text = athlete.email, style = MaterialTheme.typography.bodyMedium)
    }
}



@Composable
fun ProfileDetails(athlete: Athlete) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ProfileDetailRow(label = "Age", value = athlete.age.toString())
        // Aggiungi altre righe di dettagli come necessario
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(2f)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAthleteProfileScreen() {
    VimataTheme {

        val athlete = Athlete("","Jacopo", "Finzi","Kenzio","Jacopo@gmail","xxxxx",26)

        AthleteProfileScreen(athlete, onEditProfileClick = {})



    }
}