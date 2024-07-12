package com.amitranofinzi.vimata.ui.screen.auth


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.components.GradientBox
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AuthViewModel

@Composable
fun LoginScreen( authViewModel: AuthViewModel = AuthViewModel(), navController: NavController ) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    val isLogginIn = authState is AuthViewModel.AuthState.Loading

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
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Secondary,
                        focusedBorderColor = Secondary,
                        focusedLabelColor = Secondary,
                        focusedLeadingIconColor = Secondary
                    ),
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
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Secondary,
                        focusedBorderColor = Secondary,
                        focusedLabelColor = Secondary,
                        focusedLeadingIconColor = Secondary
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Icon"
                        )
                    },
                    keyboardOptions = KeyboardOptions(),
                    keyboardActions = KeyboardActions(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(20.dp))
                // Error message for wrong password or email
                if (authState is AuthViewModel.AuthState.Error) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error Icon",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = (authState as AuthViewModel.AuthState.Error).message,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        navController.navigate("signup")
                    },
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

                // ------------------LOGIN BUTTON---------------------
                // When clicked user credentials will be checked and:
                // - during credentials check a loading animation is visualized
                // - if credentials are valid user will be authenticated and will visualize:
                //        - AthleteHomeScreen if userType is "athlete"
                //        - TrainerHomeScreen if userType is "trainer"
                // - if credentials are not valid a error message appears
                if (isLogginIn) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            authViewModel.login(email, password)
                        },
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

            }

            LaunchedEffect(authState) {
                when (authState) {

                    is AuthViewModel.AuthState.Authenticated -> {
                        // Check user type and navigate to the correct subgraph
                        val userType = (authState as AuthViewModel.AuthState.Authenticated).userType
                        val destination = if (userType == "athlete") "athlete" else "trainer"
                        navController.navigate(destination) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                    //we can remove this
                    is AuthViewModel.AuthState.Error -> {
                        Toast.makeText(
                            context,
                            (authState as AuthViewModel.AuthState.Error).message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){
    val NavController = rememberNavController()

    VimataTheme {
        LoginScreen(navController = NavController)
    }
}