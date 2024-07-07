package com.amitranofinzi.vimata.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.data.model.FormField
import com.amitranofinzi.vimata.data.model.FormState
import com.amitranofinzi.vimata.ui.theme.Secondary

@Composable
fun UserCredentials(
    formState: FormState,
    updateField: (FormField, String) -> Unit,
    emailAlreadyUsed: (email: String) -> Unit,
    //validatePassword: (password: String) -> Unit
) {
    Column {
        /*OutlinedTextField(
            value = formState.email,
            onValueChange = { updateField(FormField.EMAIL, it) },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Secondary,
                focusedBorderColor = Secondary,
                focusedLabelColor = Secondary,
                focusedLeadingIconColor = Secondary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email"
                )
            },
        )*/
        UserInputField(
            value = formState.email,
            onValueChange = {
                updateField(FormField.EMAIL, it)
                emailAlreadyUsed(it)
                            },
            label = "Email",
            error = formState.emailError,
            errorMessage = if (formState.emailError) formState.emailErrorMessage else "",
            leadingIcon = Icons.Default.Email
        )
        Spacer(modifier = Modifier.height(8.dp))

        UserInputField(
            value = formState.password,
            onValueChange = {
                updateField(FormField.PASSWORD, it)
                emailAlreadyUsed(it)
            },
            label = "Password",
            error = formState.passwordError,
            errorMessage = if (formState.passwordError) formState.passwordErrorMessage else "",
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.confirmPassword,
            onValueChange = { updateField(FormField.CONFIRM_PASSWORD, it) },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Secondary,
                focusedBorderColor = Secondary,
                focusedLabelColor = Secondary,
                focusedLeadingIconColor = Secondary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lock"
                )
            },
        )

        Spacer(modifier = Modifier.height(8.dp))
    }

}

