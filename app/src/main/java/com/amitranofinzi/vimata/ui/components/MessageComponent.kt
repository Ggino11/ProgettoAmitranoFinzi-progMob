package com.amitranofinzi.vimata.ui.components


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.ui.theme.MessageColor
import com.amitranofinzi.vimata.ui.theme.TextColor
import java.text.SimpleDateFormat
import java.util.Locale
@Composable
fun MessageBubble(
    message: Message,
    currentUserId: String?

) {
    //currentUser will be equal to uid
    val isCurrentUser = message.senderId == currentUserId

    Column(
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = MessageColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .height(40.dp)
        ) {

            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextColor)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Log.d("Messagebubble", "Ci sono 3" )
        Text(
            text = formatTimestamp(message.timeStamp),
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
fun formatTimestamp(timestamp: com.google.firebase.Timestamp): String {
    val date = timestamp.toDate() //convert firebase format into date for formatting correctlly
    val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return simpleDataFormat.format(date)
}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewMessageBubble() {
//    val currentUserId = "user123"
//    val otherUserId = "user456"
//    val messageColorCurrentUser = Color(0xFFDCF8C6) // Example color for current user message
//    val messageColorOtherUser = Color.White // Example color for other user message
//    val textColor = Color.Black // Example text color
//
//    Column {
//        // Message from current user
//
//        MessageBubble(
//            message = Message(
//                senderId = currentUserId,
//                text = "Hello, this is a message from the current user!",
//                timeStamp = System.currentTimeMillis()
//            ),
//            currentUserId = currentUserId,
//
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Message from another user
//        MessageBubble(
//            message = Message(
//                senderId = otherUserId,
//                text = "Hello, this is a message from another user.",
//                timeStamp = System.currentTimeMillis()
//            ),
//            currentUserId = currentUserId,
//
//        )
//    }
//}
