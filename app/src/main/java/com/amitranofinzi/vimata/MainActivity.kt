package com.amitranofinzi.vimata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.amitranofinzi.vimata.ui.theme.VimataTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Applying style theme on every UI component
            VimataTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {

}