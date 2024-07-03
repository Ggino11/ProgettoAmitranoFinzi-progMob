package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.WhiteColor


//for login
@Composable
fun GradientBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.background(brush = Brush.linearGradient(
            listOf(
                Primary, WhiteColor
            )
        ))
    ){
        content()
    }
}

//for registration
@Composable
fun RegistrationGradientBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.background(brush = Brush.linearGradient(
            listOf(
                Primary, WhiteColor
            )
        ))
    ){
        content()
    }
}
