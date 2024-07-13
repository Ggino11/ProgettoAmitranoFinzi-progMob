package com.amitranofinzi.vimata.ui.components.cards


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.ui.components.ProfileAvatar
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.GrayColor
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.ui.theme.TextColor
import com.amitranofinzi.vimata.ui.theme.VimataTheme
@Composable
fun TrainerCard(
    trainer: User,
) {
    //var isNewTrainer by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(120.dp)
            .shadow(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = BgColor,
            contentColor = TextColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, Secondary),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            //isNewTrainer = false
        }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Trainer Avatar (using ProfileAvatar)
            ProfileAvatar(
                userName = trainer.name.take(1),
                userLastName = trainer.surname.take(1),
            )

            // Trainer Details
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${trainer.name} ${trainer.surname}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trainer.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trainer.userType.uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayColor
                )
//                if (isNewTrainer) {
//                    Row(
//                        modifier = Modifier
//                            .background(MessageColor, RoundedCornerShape(4.dp))
//
//                    ) {
//                        Text(
//                            text = "New Trainer",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = Secondary,
//                            modifier= Modifier.padding(horizontal = 6.dp)
//
//                        )
//                    }
//                }
            }

        }

    }
}
@Preview
@Composable
fun TrainerCardPreview() {
    VimataTheme {

        val user = User(
            email = "john.doe@example.com",
            name = "John",
            password = "hashed_password",
            surname = "Doe",
            uid = "user_id_123",
            userType = "trainer"
        )

        TrainerCard(trainer = user)
    }
}
