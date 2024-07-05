package com.amitranofinzi.vimata.ui.screen.auth

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.components.GradientBox
import com.amitranofinzi.vimata.ui.components.SimpleButton
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(authViewModel: AuthViewModel, navController: NavController) {
    //still need to understnd if better using flows
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    //change ui based on current page
    var currentPage by remember {
        mutableStateOf(0)
    }

    // Determine if the form is valid
    val isValidFormPage1 = name.isNotEmpty() && surname.isNotEmpty() && username.isNotEmpty()
    val isFormValid =  isValidFormPage1 &&  email.isNotEmpty() && password.isNotEmpty()
                        && confirmPassword.isNotEmpty() && password == confirmPassword

//
    GradientBox(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.15f), // 15 percent of the screen
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Create an Account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
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

                //first registration page
                if (currentPage == 0) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("First Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Person"
                            )
                        },
                        keyboardOptions = KeyboardOptions(),
                        keyboardActions = KeyboardActions(),

                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = surname,
                        onValueChange = { surname = it },
                        label = { Text("Last Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Person"
                            )
                        },
                        keyboardOptions = KeyboardOptions(),
                        keyboardActions = KeyboardActions()
                    )

//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    OutlinedTextField(
//                        value = username,
//                        onValueChange = { username = it },
//                        label = { Text("Username") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 12.dp),
//                        singleLine = true,
//                        leadingIcon = {
//                            Icon(
//                                imageVector = Icons.Default.FitnessCenter,
//                                contentDescription = "Person"
//                            )
//                        },
//                        keyboardOptions = KeyboardOptions(),
//                        keyboardActions = KeyboardActions()
//                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = userType,
                        onValueChange = { userType = it },
                        label = { Text("User type") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "Person"
                            )
                        },
                        keyboardOptions = KeyboardOptions(),
                        keyboardActions = KeyboardActions()
                    )

                    Spacer(modifier = Modifier.height(8.dp))



                } else {

                    //second registration page if( currentPage == 1)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        keyboardActions = KeyboardActions(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Person"
                            )
                        },

                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock"
                            )
                        },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock"
                            )
                        },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                // Dots to indicate the page position
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                if (currentPage == 0) Color.Black else Color.White,
                                RoundedCornerShape(50)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                if (currentPage == 1) Color.Black else Color.White,
                                RoundedCornerShape(50)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                SimpleButton(
                    onClick = {
                        if (currentPage == 0) {
                            currentPage = 1
                        } else {
                            authViewModel.register(email, password, userType, name, surname)
                            navController.navigate("login")
                        }
                    },

                    //enabled = if(currentPage == 0) isValidFormPage1 else isFormValid,
                    enabled = true,
                    label = if (currentPage == 0) "Next" else "Submit"
                )
            }
        }
    }
}

// Handle authentication state changes
/*
when (authState) {
    is AuthViewModel.AuthState.Loading -> {
        // Show loading indicator
    }
    is AuthViewModel.AuthState.Authenticated -> {
        // Navigate to the next screen either login or home still deciding
    }
    is AuthViewModel.AuthState.Registered -> {
        // Show registration success message
    }
    is AuthViewModel.AuthState.Error -> {
        // Show error message
        val errorMessage = (authState as AuthViewModel.AuthState.Error).message
        // Display the error
    }
    else -> {}
}
}*/
@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    val NavController = rememberNavController()

    VimataTheme{

    }
}