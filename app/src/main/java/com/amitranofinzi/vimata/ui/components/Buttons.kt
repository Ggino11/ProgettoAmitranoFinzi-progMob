package com.amitranofinzi.vimata.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.R


//This file contains several useful basic components for UI
@Composable
fun SimpleButton(onClick: () -> Unit, label: String, enabled: Boolean) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.padding(8.dp).fillMaxWidth()
    ) {
        Text(text = label)
    }
}
//@Composable
//fun ButtonFirebaseAuth(
//    modifier: Modifier = Modifier,
//    @DrawableRes icon : Int,
//    text: String,
//    onClick: () -> Unit,
//){
//    Row(
//        modifier = modifier
//            .clip(RoundedCornerShape(4.dp))
//            .background(Color(0xFFF0CECE))
//            .border(
//                width = 1.dp,
//                color = Primary
//            )
//            .clickable { onClick() }
//    ) {
//        Image (painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(16.dp))
//        Spacer(modifier = Modifier.width(5.dp))
//        Text(text =  text, style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B)))
//    }
//}
//@Preview
//@Composable
//fun ButtonFirebaseAuth() {
//   ButtonFirebaseAuth(icon = R.Drawables.goggle, text = "google" ) {
//
//   }
//}
@Composable
fun GoogleSignIn(
    text: String,
    icon: Painter,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick)
            .clip(RoundedCornerShape(4.dp)),
            //add shodow
            border = BorderStroke(width = 1.dp, color = borderColor), //doesnt work

        shadowElevation = 15.dp, //doesnt work
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = "SignInButton",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(text = text)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SignInButtonPreview() {
    GoogleSignIn(
        text = "Google",
        icon = painterResource(id = R.drawable.ic_google_logo),
        onClick = { }
    )
}