package com.amitranofinzi.vimata.ui.screen.chat

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (profileRef, messagesRef, chatBoxRef) = createRefs()


        // Profile section
        ProfileChatBar(
            userName = receiver?.name,
            userLastName = receiver?.surname,
            onBackClicked = { navController.popBackStack() },
            modifier = Modifier.constrainAs(profileRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(messagesRef.top)
                width = Dimension.fillToConstraints
            }
        )
        Text(messages.toString())
        // Messages section
        LazyColumn(
            modifier = Modifier.constrainAs(messagesRef) {
                top.linkTo(profileRef.bottom)
                bottom.linkTo(chatBoxRef.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {
            items(messages) { message ->
                Log.d("SingleChatScreen", "Messaggio in lazy column: ${message.toString()}")
                Column {
                    MessageBubble(
                        message = message,
                        currentUserId = senderId
                    )
                }
            }
        }

        // Chat box section
        Column(
            modifier = Modifier.constrainAs(chatBoxRef) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        ) {
            chatId?.let {

                receiver?.uid?.let { it1 ->
                    ChatBox(
                        senderId = senderId,
                        receiverId = it1,
                        chatId = it,
                        onSend = { chatId: String, message: Message ->
                            chatViewModel.sendMessage(chatId, message)
                        }
                    )
                }
            }

        }
    }

}


/*
@Composable
@Preview(showBackground = true)
fun SingleChatScreenPreview() {

   VimataTheme {
       val user = User("userId", "John", "Doe","doe","fnweoi","trainer") // Esempio di utente
       val message = Message("chatId", "userId", "text",
           java.sql.Timestamp(System.currentTimeMillis())) // Esempio di messaggio
       val chatViewModel = ChatViewModel() // ViewModel
       SingleChatScreen(
           user = user,
           message = message,
           chatId = "chatId", // Esempio di ID della chat
           chatViewModel = chatViewModel
       )
   }

}
*/