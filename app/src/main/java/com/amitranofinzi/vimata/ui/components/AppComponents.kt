package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun StandardTextComponent(value:String){
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp)
    )
}