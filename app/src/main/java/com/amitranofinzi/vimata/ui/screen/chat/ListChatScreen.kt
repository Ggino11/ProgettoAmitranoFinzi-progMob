package com.amitranofinzi.vimata.ui.screen.chat

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.amitranofinzi.vimata.ui.components.ChatTopBar
import com.amitranofinzi.vimata.ui.components.mockChats
import com.amitranofinzi.vimata.ui.theme.VimataTheme


@Composable
fun ListChatScreen(){

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (topBarRef, listRef) = createRefs()

        //top bar section
        ChatTopBar(
            modifier = Modifier.constrainAs(topBarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(listRef.top)
                width = Dimension.fillToConstraints

            }
        )
        //list chat section
        LazyColumn(
            modifier = Modifier.constrainAs(listRef) {
                top.linkTo(topBarRef.bottom)
                height = Dimension.fillToConstraints



            }
        ) {
            items(mockChats) { mockChat ->
                Log.d("Chat1", "sono dentro" )
                Column {
                   //ChatPreview(chat= mockChat, openChat = {}, user = fire)
                    HorizontalDivider()
                }
            }
        }
}}

@Composable
@Preview
fun Preview () {
    VimataTheme {
        ListChatScreen()
    }
}