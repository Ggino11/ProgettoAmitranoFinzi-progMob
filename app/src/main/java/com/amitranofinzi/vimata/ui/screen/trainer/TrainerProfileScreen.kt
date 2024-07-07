package com.amitranofinzi.vimata.ui.screen.trainer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.viewmodel.AuthViewModel
import com.amitranofinzi.vimata.viewmodel.TrainerViewModel


@Composable
fun TrainerProfileScreen(onEditProfileClick: () -> Unit,
                         trainerViewModel: TrainerViewModel = TrainerViewModel(),
                         authViewModel: AuthViewModel = AuthViewModel(),
                         navController: NavController) {
    Button(
        onClick = {
            authViewModel.signOut()
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
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
            text = "Sign out",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500))
        )
    }

}
