package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.R
import com.amitranofinzi.vimata.data.model.FormField
import com.amitranofinzi.vimata.data.model.FormState
import com.amitranofinzi.vimata.ui.theme.Secondary

@Composable
fun UserDetails(
    formState: FormState,
    updateField: (FormField, String) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center, // Center horizontally
            modifier = Modifier
                .fillMaxWidth()
        ) {
        Box(
            modifier = Modifier
                .size(150.dp),
            contentAlignment = Alignment.Center,

            ) {
            Image (
                painter = painterResource(id = R.drawable.registration),
                contentDescription = "registration image",
                modifier = Modifier.size(180.dp)
            )
        }
    }

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = formState.name,
            onValueChange = { updateField(FormField.NAME, it) },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding( 12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Secondary,
                focusedBorderColor = Secondary,
                focusedLabelColor = Secondary,
                focusedLeadingIconColor = Secondary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person"
                )
            },
        )

//        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.surname,
            onValueChange = { updateField(FormField.SURNAME, it) },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Secondary,
                focusedBorderColor = Secondary,
                focusedLabelColor = Secondary,
                focusedLeadingIconColor = Secondary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person"
                )
            },
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
