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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.data.model.FormField
import com.amitranofinzi.vimata.ui.components.GradientBox
import com.amitranofinzi.vimata.ui.components.SimpleButton
import com.amitranofinzi.vimata.ui.components.UserInputField
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(authViewModel: AuthViewModel, navController: NavController) {

    val formState by authViewModel.formState.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    var currentPage by remember { mutableIntStateOf(0) }
    val context = LocalContext.current


    // Determine if the form is valid
//    val isValidFormPage1 = formState.name.isNotEmpty() && formState.surname.isNotEmpty() && formState.u
//    val isFormValid =  isValidFormPage1 &&  formState.email.isNotEmpty() && formState.password.isNotEmpty()
//                        && formState.confirmPassword.isNotEmpty() && formState.password == formState.confirmPassword

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
                        value = formState.name,
                        onValueChange = { authViewModel.updateField(FormField.NAME, it) },
                        label = { Text("First Name")},
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
                        value = formState.surname,
                        onValueChange = { authViewModel.updateField(FormField.SURNAME, it) },
                        label = { Text("Last Name")},
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    //creare componente con due bottoni
                    OutlinedTextField(
                        value = formState.userType,
                        onValueChange = { authViewModel.updateField(FormField.USER_TYPE, it) },
                        label =  {Text("UserType")},
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
                    UserInputField(
                        value = formState.email,
                        onValueChange = { authViewModel.updateField(FormField.EMAIL, it) },
                        label = "Email" ,
                        error = formState.emailError,
                        errorMessage = "Email already exists",
                        leadingIcon = Icons.Default.Email
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = formState.password,
                        onValueChange = {authViewModel.updateField(FormField.PASSWORD, it)  },
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
                        value = formState.confirmPassword,
                        onValueChange = { authViewModel.updateField(FormField.CONFIRM_PASSWORD, it) },
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
                                if (currentPage == 0) Secondary else Color(0xFF81A4B6),
                                RoundedCornerShape(50)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                if (currentPage == 1) Secondary else Color(0xFF81A4B6),
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
                            authViewModel.register(formState)
                            navController.navigate("Login")

                        }
                    },

                    // enabled = if(currentPage == 0) isValidFormPage1 else isFormValid,
                    enabled = true,
                    label = if (currentPage == 0) "Next" else "Submit"
                )
            }
        }
    }
    // Handle authentication state changes
//    LaunchedEffect(authState) {
//        when (authState) {
//            is AuthViewModel.AuthState.Authenticated -> {
//                // Check user type and navigate to the correct subgraph
//                val userType = (authState as AuthViewModel.AuthState.Authenticated).userType
//                val destination = if (userType == "athlete") "athlete" else "trainer"
//                navController.navigate(destination) {
//                    popUpTo(navController.graph.startDestinationId) {
//                        inclusive = true
//                    }
//                }
//            }
//            is AuthViewModel.AuthState.Error -> {
//                Toast.makeText(context, (authState as AuthViewModel.AuthState.Error).message, Toast.LENGTH_LONG).show()
//            }
//            else -> Unit
//        }
//    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    val NavController = rememberNavController()


    VimataTheme{

    }
}