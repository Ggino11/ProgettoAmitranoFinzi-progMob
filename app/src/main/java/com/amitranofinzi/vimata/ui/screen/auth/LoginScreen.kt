package com.amitranofinzi.vimata.ui.screen.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.ui.components.SimpleButton
import com.amitranofinzi.vimata.ui.components.SimpleTextField
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.LoginViewModel

@Composable
fun LoginScreen(/*loginViewModel: LoginViewModel = LoginViewModel()*/) {
    //val loginState by loginViewModel.loginState.observeAsState()

    var email = "jacopomail.com" //by remember { mutableStateOf("") }
    var password = "jacopopsw" // by remember { mutableStateOf("") }

    // UI della pagina di login
    Column(modifier = Modifier.padding(16.dp)) {
        SimpleTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
        )
        SimpleTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )
        SimpleButton(
            onClick = { /*loginViewModel.login(email, password)*/ },
            label = "Login"
        )
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
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    VimataTheme {
        LoginScreen()
    }
}