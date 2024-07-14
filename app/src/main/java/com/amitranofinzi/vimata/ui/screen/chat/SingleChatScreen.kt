package com.amitranofinzi.vimata.ui.screen.chat

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.ui.components.ChatBox
import com.amitranofinzi.vimata.ui.components.MessageBubble
import com.amitranofinzi.vimata.ui.components.ProfileChatBar
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.ChatViewModel


@Composable
fun SingleChatScreen(
    authViewModel: AuthViewModel = AuthViewModel(),
    chatViewModel: ChatViewModel = ChatViewModel(),
    chatId: String?,
    receiverId: String?,
    navController: NavController
) {
    val messages by chatViewModel.messages.collectAsState()
    val receiver by chatViewModel.receiver.observeAsState()
    val senderId = authViewModel.getCurrentUserID()
    val listState = rememberLazyListState()

    LaunchedEffect(chatId) {
        if (chatId != null) {
            chatViewModel.listenForMessages(chatId)
        }
    }

    LaunchedEffect(receiverId) {
        if (receiverId != null) {
            chatViewModel.fetchReceiverById(receiverId)
        }

    }



    Log.d("SingleChatScreen", "Messages: ${messages.size}")
    Log.d("SingleChatScreen", "Messages: ${messages.toString()}")

    Log.d("SingleChatScreen", receiver.toString())


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Profile section
        ProfileChatBar(
            userName = receiver?.name,
            userLastName = receiver?.surname,
            onBackClicked = { navController.popBackStack() },
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )

        // Messages section
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState,
        ) {
            items(messages) { message ->
                Log.d("Rendering messages", message.text)
                Column {
                    MessageBubble(
                        message = message,
                        currentUserId = senderId
                    )
                }
            }
        }

        // Chat box section
        if (receiverId != null && chatId != null) {
            ChatBox(
                senderId = senderId,
                receiverId = receiverId,
                chatId = chatId,
                onSend = { chatId: String, message: Message ->
                    chatViewModel.sendMessage(chatId, message)
                }
            )
        }
    }
}

