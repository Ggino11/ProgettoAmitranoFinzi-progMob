package com.amitranofinzi.vimata.ui.screen.auth

import android.view.ViewTreeObserver
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amitranofinzi.vimata.ui.components.GradientBox
import com.amitranofinzi.vimata.ui.components.SimpleTextField
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.LoginViewModel


@Composable
fun LoginScreen(loginViewModel: LoginViewModel = LoginViewModel()) {
    //val loginState by loginViewModel.loginState.observeAsState()
    val isImeVisible by rememberImeState()
    GradientBox(modifier= Modifier.fillMaxSize()) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally){
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f), //35 percent of the screen
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Welcome to  Vimata",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(BgColor),
                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black)

                Spacer(modifier = Modifier.fillMaxSize(0.1f))

            SimpleTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = "",
                onValueChange = {},
                label = "Username/Email",
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions()
            )
            Spacer(modifier = Modifier.height(20.dp))
            SimpleTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = "",
                onValueChange = {},
                label = "Password",
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions()
            )
                if (isImeVisible) {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.fillMaxWidth().padding(top = 20.dp).padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Log in",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500))
                        )
                    }

                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Login",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500))
                            )
                        }
                    }
                }

            }
        }

    }


}


//collapse red part when keybor
@Composable
fun rememberImeState(): State<Boolean> {
    val imeState = remember {
        mutableStateOf(false)
    }
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            imeState.value = isKeyboardOpen
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return imeState
}

    // Gestione dello stato del login
    /*
    when (loginState) {
        is LoginViewModel.LoginState.Success -> {
            // Naviga alla schermata successiva o mostra un messaggio di successo
        }
        is LoginViewModel.LoginState.Error -> {
            // Mostra un messaggio di errore
        }
        else -> Unit
    }
    */

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){
    VimataTheme {
        LoginScreen()
    }
}