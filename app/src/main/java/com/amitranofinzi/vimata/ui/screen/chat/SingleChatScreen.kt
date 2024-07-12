package com.amitranofinzi.vimata.ui.screen.chat

/*
@Composable
fun SingleChatScreen(
    authViewModel: AuthViewModel = AuthViewModel(),
    chatViewModel: ChatViewModel = ChatViewModel(),
    chatId: String?,
    navController: NavController
) {
//    val messages: List<Message> by chatViewModel.messages.observeAsState(emptyList())
    val messages by chatViewModel.messages.collectAsState()
    val user by authViewModel.user.observeAsState()
    //actually is sender
    val senderId = authViewModel.getCurrentUserID()
    val receiverId by chatViewModel.receiverId.observeAsState()

    LaunchedEffect(chatId){
        if (chatId != null) {
            chatViewModel.listenForMessages(chatId)
        }
    }


    LaunchedEffect(chatId, user?.userType){
        if (chatId != null) {
            chatViewModel.fetchReceiverId(chatId, user!!.userType)
        }
    }

    LaunchedEffect(receiverId){
        receiverId?.let { authViewModel.getUser(it) }
    }
    Log.d("SingleChatScreen0",receiver.toString())

    Log.d("SingleChatScreen1","Navigate")
    //FETCH MESSAGES using flow

    Log.d("SingleChatScreen2", messages.isEmpty().toString())
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
            })
        {
            chatId?.let {
                if (receiverId != null) {
                    ChatBox(
                        senderId = senderId,
                        receiverId = receiverId!!,
                        chatId = it,
                        onSend = { chatId: String, message: Message ->
                            chatViewModel.sendMessage(chatId, message )
                        }
                    )
                }
            }

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