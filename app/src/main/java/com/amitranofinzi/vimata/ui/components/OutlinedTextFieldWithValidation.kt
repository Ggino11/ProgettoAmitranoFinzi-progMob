package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.ui.theme.Primary
import com.amitranofinzi.vimata.ui.theme.Secondary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: Boolean = false,
    errorMessage: String = "",
    leadingIcon: ImageVector,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )
            },
            isError = error,
            visualTransformation = visualTransformation,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (error) Primary else MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = if (error) Primary else MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = if (error) Primary else MaterialTheme.colorScheme.secondary,
                focusedLeadingIconColor = if (error) Primary else MaterialTheme.colorScheme.secondary,
                cursorColor = Secondary,
            )
        )
        if (error) {
            Row(modifier = Modifier.padding(start = 12.dp, top = 4.dp)) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserInputField() {
    MaterialTheme {
        UserInputField(
            value = "",
            onValueChange = {it},
            label = "Email",
            error = false,
            errorMessage = "Email already in use",
            leadingIcon = Icons.Default.Email
        )
    }
}
