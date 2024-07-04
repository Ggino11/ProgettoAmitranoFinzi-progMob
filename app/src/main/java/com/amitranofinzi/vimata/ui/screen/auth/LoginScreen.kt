package com.amitranofinzi.vimata.ui.screen.auth


import android.view.ViewTreeObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.R
import com.amitranofinzi.vimata.ui.components.GoogleSignIn
import com.amitranofinzi.vimata.ui.components.GradientBox
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AuthViewModel

@Composable
fun LoginScreen( authViewModel: AuthViewModel = AuthViewModel(), navController: NavController) {
    val isImeVisible by rememberImeState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



    GradientBox(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f), // 35 percent of the screen
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Welcome to Vimata",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(BgColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "SIGN IN",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold

                )

                Spacer(modifier = Modifier.fillMaxSize(0.1f))


                OutlinedTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Username/Email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Icon"
                        )
                    },
                    keyboardOptions = KeyboardOptions(),
                    keyboardActions = KeyboardActions()
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Icon"
                        )},
                    keyboardOptions = KeyboardOptions(),
                    keyboardActions = KeyboardActions(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(20.dp))
                /*
                RegisterText( onRegisterClick = {
                    // Handle register navigation here

                    Log.d("RegisterText", "ClickableText clicked: $")

                    navController.navigate("signup") {

                    }
                })
                */
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {

                        navController.navigate("signup")

                    }

                    ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Secondary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = "Sign up",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500))
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                if (isImeVisible) {
                    Button(
                        // L'
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .padding(horizontal = 16.dp),
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

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Divider(modifier = Modifier.weight(1f))
                        Text(
                            text = "or",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Color.Gray
                        )
                        Divider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                      GoogleSignIn(text = "Google", icon = painterResource(id = R.drawable.ic_google_logo) ) {
                          
                      }

                        Spacer(modifier = Modifier.width(16.dp))

                        GoogleSignIn(text = "Google",
                            icon = painterResource(id = R.drawable.ic_google_logo),
                            onClick = { /*handle auth */}
                        )
                    }

                } else {
                        Button(
                            onClick = {

                                navController.navigate("athlete_screen") {
                                    popUpTo("athlete") {
                                        inclusive = true
                                    }
                                }

                            }

                       ,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text(
                                text = "Login",
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500))
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Divider(modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 20.dp))
                        Text(
                            text = "or",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Color.Gray
                        )
                        Divider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        GoogleSignIn(text = "Google",
                            icon = painterResource(id = R.drawable.ic_google_logo),
                            onClick = { TODO() }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        GoogleSignIn(text = "Google",
                            icon = painterResource(id = R.drawable.ic_google_logo),
                            onClick = { TODO() }
                        )
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
    val NavController = rememberNavController()

    VimataTheme {
        LoginScreen(navController = NavController)
    }
}