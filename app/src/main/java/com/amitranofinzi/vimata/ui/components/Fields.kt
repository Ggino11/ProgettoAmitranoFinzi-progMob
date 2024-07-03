package com.amitranofinzi.vimata.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    readOnly: Boolean = false,
    height:Dp =  42.dp,
    trailingIcon: ImageVector? = null
) {
    Column(modifier = modifier) {
        Text(text = label)
        Spacer(modifier = Modifier.height(10.dp))
        BasicTextField(
            value = "",
            onValueChange = {},
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            readOnly = readOnly,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .height(height)
                    .background(Color(0xFFEFEEEE)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f).padding(horizontal = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    it.invoke()
                }
                trailingIcon?.let {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = trailingIcon, contentDescription = null, tint = Color(0xFF828282))
                    }
                }
            }
        }
    }
}

//register text for when u dont have an account
@Composable
fun RegisterText(onRegisterClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Gray)) {
            append("Don't have one? ")
        }
        withStyle(style = SpanStyle(color = Color.Black, textDecoration = TextDecoration.Underline)) {
            append("Register")
        }
    }

    ClickableText(
        text = annotatedText,

        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "REGISTER",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onRegisterClick()
            }
        },
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}