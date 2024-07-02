package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//This file contains several useful basic components for UI
@Composable
fun SimpleButton(onClick: () -> Unit, label: String) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = label)
    }
}
@Preview
@Composable
fun SimpleButton() {
    SimpleButton(onClick = { /*TODO*/ }, label = "")
}