package com.amitranofinzi.vimata.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.ui.components.ProfileAvatar
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.GrayColor
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.ui.theme.TextColor

@Composable
fun AthleteCard(athlete: User, modifier: Modifier, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .shadow(6.dp)
            .clickable {

            },
        colors = CardDefaults.cardColors(
            containerColor = BgColor,
            contentColor = TextColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Secondary),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Athlete Avatar (using ProfileAvatar)
            ProfileAvatar(
                userName = athlete.name.take(1),
                userLastName = athlete.surname.take(1),
            )

            // Athlete Details
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "${athlete.name} ${athlete.surname}",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = athlete.userType,
                    color = GrayColor
                )
            }

            IconButton(
                onClick = {  navController.navigate("athleteHandler/${athlete.uid}")},
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(imageVector = Icons.Default.Visibility, contentDescription = "View Icon")
            }
        }
    }

}


@Preview
@Composable
fun AthleteCardPreview() {
    val athlete = User(
        email = "athlete@example.com",
        name = "Athlete",
        password = "hashed_password",
        surname = "Doe",
        uid = "athlete_id_123",
        userType = "ATHLETE"
    )

    val modifier = Modifier.fillMaxWidth()
    val navController = rememberNavController()
    AthleteCard(athlete = athlete, modifier = modifier, navController=navController)
}