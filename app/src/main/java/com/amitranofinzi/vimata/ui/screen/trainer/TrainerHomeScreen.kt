package com.amitranofinzi.vimata.ui.screen.trainer

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.amitranofinzi.vimata.ui.theme.VimataTheme

@Composable
fun HomeTrainerScreen() {
    // Pagina con le mie schede e i miei allenatori
    Column {
        // Aggiungi contenuto per le schede
        Text("I miei atleti")
        // Aggiungi contenuto per gli allenatori
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeTrainerScreen() {
    VimataTheme {
        HomeTrainerScreen()
    }
}