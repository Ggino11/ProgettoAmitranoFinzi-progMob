package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.FormField
import com.amitranofinzi.vimata.data.model.FormState
import com.amitranofinzi.vimata.ui.theme.BgColor
import com.amitranofinzi.vimata.ui.theme.Secondary
import com.amitranofinzi.vimata.ui.theme.VimataTheme


@Composable
fun UserType(
    formState: FormState,
    updateField: (FormField, String) -> Unit,
) {

    var selectedStateTrainer by remember { mutableStateOf(false) }
    var selectedStateAthlete by remember { mutableStateOf(false) }


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Register as:",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(vertical = 15.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            CardOption(
                title = "Coach",
                isSelected = selectedStateTrainer,
                onClick = {
                    selectedStateTrainer = !selectedStateTrainer
                    updateField(FormField.USER_TYPE, "trainer")
                    selectedStateAthlete = false
                            }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp))
                Text(
                    text = "or",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray
                )
                Divider(modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 3.dp))
            }
            Spacer(modifier =  Modifier.height(20.dp))

            CardOption(
                title = "Athlete",
                isSelected = selectedStateAthlete,
                onClick = {
                    selectedStateAthlete = !selectedStateAthlete
                    selectedStateTrainer = false
                    updateField(FormField.USER_TYPE, "athlete")
                }
            )
        }
    }


@Composable
fun CardOption(title: String,  isSelected: Boolean, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)// Increase the height of the card
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        border = if (isSelected) BorderStroke(2.dp, Secondary) else BorderStroke(2.dp, Color.Transparent),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ), //elevation card
        colors = CardDefaults.cardColors(
            containerColor = BgColor
        )


    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center // Correct TextAlign import
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun UserTypePreview() {
    VimataTheme {

    }
}