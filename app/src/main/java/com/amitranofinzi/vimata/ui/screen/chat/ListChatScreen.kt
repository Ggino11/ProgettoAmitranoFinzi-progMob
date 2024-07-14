package com.amitranofinzi.vimata.ui.screen.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.ui.components.ChatPreview
import com.amitranofinzi.vimata.ui.components.ChatTopBar
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.ChatViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListChatScreen(
    chatViewModel: ChatViewModel = ChatViewModel(),
    authViewModel: AuthViewModel = AuthViewModel(),
    navController: NavController
){
    val relationships by chatViewModel.relationships.observeAsState(emptyList())
    val chats by chatViewModel.chats.observeAsState(emptyList())
    // receivers is the list of all users related to current user
    val receivers by chatViewModel.receivers.observeAsState(emptyList())


    // Current user data
    val userID = authViewModel.getCurrentUserID()
    val user by authViewModel.user.observeAsState()

    // Fetch current user, stored in user
    LaunchedEffect(userID) {
        authViewModel.fetchUser(userID)
    }

    // Fetch all relationships in which current user is present
    // We use userType to know on which attribute query (athleteID or trainerID)
    val userType = user?.userType
    LaunchedEffect(userID, userType) {
        if (userType != null) {
            chatViewModel.fetchRelationships(userID, userType)
            chatViewModel.fetchReceivers(userID, userType)
        }
    }

    Log.d("ListChatScreen", "receivers: ${receivers.toString()}")


    val relationshipIds: List<String> = relationships.map { it.id }
    LaunchedEffect(relationshipIds) {
        chatViewModel.fetchChats(relationshipIds)
    }

    Log.d("ListChatScreen", "receivers: ${chats.toString()}")

    Scaffold(
        topBar = {
            ChatTopBar()
        }
    ){
        Column {
            Text(
                text = "Chat",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start)
            )
            Log.d("ListChatScreen", chats.toString())
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                val receiversList = receivers ?: emptyList()

                items(chats.zip(receiversList)) { (chat, receiver) ->
                    ChatPreview(
                        chat = chat,
                        openChat = {
                            Log.d("ListChatScreen.openChat", chat.chatId)
                            // Ensure receiver is not null before accessing its uid
                            receiver?.let {
                                navController.navigate("chatDetails/${chat.chatId}/${it.uid}")
                            }
                        },
                        user = receiver
                    )
                }
            }
        }
    }
}


/*
@Composable
@Preview
fun Preview () {
    VimataTheme {
        ListChatScreen()
    }
}
*/