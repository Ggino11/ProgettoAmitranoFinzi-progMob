package com.amitranofinzi.vimata.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amitranofinzi.vimata.ui.components.GradientBox
import com.amitranofinzi.vimata.ui.components.SimpleButton
import com.amitranofinzi.vimata.ui.components.UserCredentials
import com.amitranofinzi.vimata.ui.components.UserDetails
import com.amitranofinzi.vimata.ui.components.UserType
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignUpScreen(authViewModel: AuthViewModel, navController: NavController) {

    val formState by authViewModel.formState.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()



    val isFormValid = formState.userType.isNotEmpty() && formState.name.isNotEmpty() && formState.surname.isNotEmpty() &&formState.email.isNotEmpty() && formState.password.isNotEmpty() &&
            formState.confirmPassword.isNotEmpty() && formState.password == formState.confirmPassword

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
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { page ->
                    when (page) {
                        0 -> UserType(
                            formState = formState,
                            updateField = authViewModel::updateField
                        )
                        1 -> UserDetails(
                            formState = formState,
                            updateField = authViewModel::updateField
                        )
                        2 -> UserCredentials(
                            formState = formState,
                            updateField = authViewModel::updateField,
                            emailAlreadyUsed = authViewModel::emailAlreadyUsed
                             //validatePassword = authViewModel::validatePassword
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dots to indicate the page position
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { page ->
                        val color = if (pagerState.currentPage == page) Secondary else Color(0xFF81A4B6)
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color, RoundedCornerShape(50))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                SimpleButton(
                    onClick = {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            authViewModel.register(formState)
                        }
                    },
                    enabled = if (pagerState.currentPage == 2) isFormValid else true,
                    label = if (pagerState.currentPage < pagerState.pageCount - 1) "Next" else "Submit"
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
            else -> Unit
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {

    val NavController = rememberNavController()


    VimataTheme {
        SignUpScreen(authViewModel = AuthViewModel(), navController = NavController )
    }
}