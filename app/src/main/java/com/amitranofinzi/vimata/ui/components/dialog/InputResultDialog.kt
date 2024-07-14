package com.amitranofinzi.vimata.ui.components.dialog

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amitranofinzi.vimata.ui.theme.MessageColor
import com.amitranofinzi.vimata.ui.theme.VimataTheme


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputResultDialog(
        onResultEntered: (Double) -> Unit,
        onDismissRequest: () -> Unit
){
    var resultValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = "Insert Results",
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (resultValue.isNotEmpty()) {
                        // Converte il valore in Double e chiama la callback per comunicare il risultato
                        val result = resultValue.toDoubleOrNull()
                        if (result != null) {
                            Log.d("InputResult", "Input valido ${result}")
                            onResultEntered(result)
                            resultValue = ""
                        } else {
                            // Gestione dell'errore se l'input non è valido
                            Log.d("InputResult", "Error input non valido")
                        }
                    }
                    else
                    Log.d("InputResult", "Error resultValue è empty")

                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Risultato:")
                // Campo di input per il risultato
                // In questo esempio, un campo di testo per il numero
                TextField(
                    value = resultValue,
                    onValueChange = { resultValue = it },
                    label = { Text("Inserisci il numero") },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MessageColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewInputResultDialog() {
    VimataTheme {
        InputResultDialog(
            onResultEntered = { result ->
                Log.d("Preview", "Result entered: $result")
            },
            onDismissRequest = {
                Log.d("Preview", "Dialog dismissed")
            }
        )
    }
}