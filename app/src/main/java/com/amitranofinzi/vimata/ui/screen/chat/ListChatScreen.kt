package com.amitranofinzi.vimata.ui.screen.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.ChatViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListChatScreen(
    chatViewModel: ChatViewModel = ChatViewModel(),
    authViewModel: AuthViewModel = AuthViewModel(),
    navController: NavController
){


    val user by authViewModel.user.observeAsState()
    val relationships by chatViewModel.relationships.observeAsState(emptyList())
    val chats by chatViewModel.chats.observeAsState(emptyList())


    val userID = authViewModel.getCurrentUserID()

    Log.d("ListChatScreen", userID)

    LaunchedEffect(userID) {
        authViewModel.fetchUser(userID)
    }
    Log.d("ListChatScreen", user.toString())

    val userType = user?.userType
    LaunchedEffect(userID, userType) {
        if (userType != null) {
            chatViewModel.fetchRelationship(userID, userType)
        }
    }
    Log.d("ListChatScreen", relationships.toString())

    val relationshipIds: List<String> = relationships.map { it.id }

    LaunchedEffect(relationshipIds) {
        chatViewModel.fetchChats(relationshipIds)
    }

    Log.d("ListChatScreen", chats.toString())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Show AddCollectionDialog */ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Collection")
            }
        }
    ){
        Column {
            Text(
                text = "Workbook",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start)
            )

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(chats) { chat ->
                    Log.d("TrainerWorkbookScreen", chat.chatId)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("chatDetails/${chat.chatId}")
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(chat.lastMessage)
                        }
                    }
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