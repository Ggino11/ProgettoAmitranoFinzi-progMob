package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.MessageColor
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.ui.theme.VimataTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(
//    messageText: String,
//    onSend: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgColor)

    ){
        TextField(
            value = "",
            onValueChange = {it},
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),

            placeholder = { Text("Type a message") },
            shape = RoundedCornerShape(48f),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MessageColor, // Access secondary color from theme
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
            )
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = {
//                if (messageText.isNotBlank()) {
//                    onSendMessage(messageText)
//                    messageText = ""
//                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Send, contentDescription = "Send",
                tint = Secondary)
        }
    }
}
@Composable
@Preview(showBackground = true)
fun previewChatBox() {
    VimataTheme {
        ChatBox()
     }
}