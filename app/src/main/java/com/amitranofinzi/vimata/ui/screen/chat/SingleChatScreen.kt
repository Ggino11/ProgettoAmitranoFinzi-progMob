package com.amitranofinzi.vimata.ui.screen.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.ui.components.MessageBubble
import com.amitranofinzi.vimata.ui.components.ProfileChatBar
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.ChatViewModel


@Composable
fun SingleChatScreen(
    authViewModel: AuthViewModel = AuthViewModel(),
    chatViewModel: ChatViewModel = ChatViewModel(),
    chatId: String?,
    navController: NavController
) {
    val messages: List<Message> by chatViewModel.messages.observeAsState(emptyList())
    //val newMessage = remember { Message(senderId = user.uid, chatId = "chatId", text = "") }
    val user by authViewModel.user.observeAsState()

    //FETCH MESSAGES

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (profileRef, messagesRef, chatBoxRef) = createRefs()

        // Profile section
        ProfileChatBar(
            userName = user?.name,
            userLastName = user?.surname,
            onBackClicked = { /*NAVIGATION*/ },
            modifier = Modifier.constrainAs(profileRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(messagesRef.bottom)
                width = Dimension.fillToConstraints
            }
        )

        // Messages section
        LazyColumn(
            modifier = Modifier.constrainAs(messagesRef) {
                top.linkTo(profileRef.bottom)
                top.linkTo(profileRef.bottom)
                bottom.linkTo(chatBoxRef.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {
            items(messages) { message ->
                Column {
                    MessageBubble(
                        message = message,
                        currentUserId = user?.uid

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
            })
        {
//            ChatBox(
//                message = message,
//                onSend = { message ->
//                    chatViewModel.sendMessage(message.chatId, message)
//                }
//            )

        }
    }
}


//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)) {
//
//
//        if (chatId != null) {
//            Text(text = chatId)
//        }
//    }
//}


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