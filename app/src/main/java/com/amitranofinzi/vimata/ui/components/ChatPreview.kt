package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.TextColor
import com.amitranofinzi.vimata.ui.theme.VimataTheme

// Esempio di dati mock per le chat
val mockChats = listOf(
    Chat("chat1", "fwij33848 ", "Hello!"),
    Chat("chat2", "fwij33848 ", "Hi there!"),
    Chat("chat3", "fwij33848 ", "How are you?") ,
    Chat("chat3", "fwij33848 ", "How are you?"),
)


@Composable
fun ChatPreview(
    chat: Chat,
    openChat: () -> Unit,
    user: User,
) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .clickable { openChat() },

        ) {
            // Profile Avatar
            ProfileAvatar(
                userName = user.name,
                userLastName = user.surname,
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Last message
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "${user.name} ${user.surname}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = BgColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = chat.lastMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextColor,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
            IconButton(
                onClick = { },
                modifier = Modifier.size(36.dp),
                content = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            )
        }

}

@Preview(showBackground = true)
@Composable
fun ChatPreviewPreview() {
    VimataTheme {

        val user = User(
            email = "john.doe@example.com",
            name = "John",
            password = "hashed_password",
            surname = "Doe",
            uid = "user_id_123",
            userType = "TRAINER"
        )
    ChatPreview(chat = mockChats[0], openChat = {}, user = user)
}
}