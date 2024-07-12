package com.amitranofinzi.vimata

import android.os.Bundle
import androidx.activity.compose.setContent
import com.amitranofinzi.vimata.ui.navigation.AppNav
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.google.firebase.FirebaseApp





class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {


            // Applying style theme on every UI component
            VimataTheme {
                AppNav()
            }

        }
    }
}

