package com.amitranofinzi.vimata.ui.screen.athlete

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.amitranofinzi.vimata.ui.theme.VimataTheme

@Composable
fun HomeAthleteScreen() {
    // Pagina con le mie schede e i miei allenatori
    Column {
        Text("Le mie schede")
        // Aggiungi contenuto per le schede
        Text("I miei allenatori")
        // Aggiungi contenuto per gli allenatori
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeAthleteScreen() {
    VimataTheme {
        HomeAthleteScreen()
    }
}