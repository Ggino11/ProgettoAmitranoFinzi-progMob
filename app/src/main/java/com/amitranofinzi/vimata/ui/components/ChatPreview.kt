package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.MessageColor
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.ChatViewModel

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
                    .fillMaxWidth()
                    .padding(2.dp)
                    .background(MessageColor)
                    .clickable{openChat()}
            ) {
                // Profilo Avatar
                ProfileAvatar(
                    userName = user.name, // Prendiamo solo il primo partecipante per semplicità
                    userLastName = user.surname
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Ultimo messaggio
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    shape = RoundedCornerShape(48f),
                    color = BgColor
                ) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            text = chat.lastMessage,
                            color = Color.Black
                        )
                    }
                }
            }


}

@Composable
@Preview(showBackground = true)
fun ListChatPreview() {
    VimataTheme {
    val viewModel = ChatViewModel()
        val user = User("icoai","w3eojf","ewuioh","nueiw","vnwuieo,","trainer")

    ChatPreview(
        chat = mockChats[0],
        openChat = {},

        user = user
    )

    }
}