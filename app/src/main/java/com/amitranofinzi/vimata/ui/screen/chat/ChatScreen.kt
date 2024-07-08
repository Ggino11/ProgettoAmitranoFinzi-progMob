/*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.ui.components.MessageBubble

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(
    userName: String,
    userLastName: String,
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onBackClicked: () -> Unit
) {

}

@Composable
fun MessageColumn(
    messages: List<Message>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(messages.size) { index ->
            MessageBubble(
                message = messages[index],
                currentUserId = "user1" // Replace with actual user ID
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(
        userName = "John",
        userLastName = "Doe",
        messages = listOf(
            Message("Hello!"),
            Message("Hi there!")
        ),
        onSendMessage = { /* Handle sending message */ },
        onBackClicked = { /* Handle back navigation */ }
    )
}
/**/