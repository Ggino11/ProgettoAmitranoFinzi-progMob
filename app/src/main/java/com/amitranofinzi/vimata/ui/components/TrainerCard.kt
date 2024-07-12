package com.amitranofinzi.vimata.ui.components


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.GrayColor
import com.amitranofinzi.vimata.ui.theme.TextColor
import com.amitranofinzi.vimata.ui.theme.VimataTheme


@Composable
fun TrainerCard(
    trainer: User,
) {
    Card(
        modifier = Modifier .padding(16.dp).shadow(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = BgColor, // Set background color
            contentColor = TextColor // Set text color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically,

        ) {
            // Trainer Avatar (using ProfileAvatar)
            ProfileAvatar(
                userName = trainer.name?.take(1),
                userLastName = trainer.surname?.take(1),

            )

            // Trainer Details
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "${trainer.name} ${trainer.surname}",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trainer.userType,

                    color = GrayColor
                )
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
            userType = "TRAINER"
        )
    val modifier = Modifier
    TrainerCard(trainer = user, )
    }
}

