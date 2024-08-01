package com.amitranofinzi.vimata

import android.os.Bundle
import androidx.activity.compose.setContent
import com.amitranofinzi.vimata.data.database.AppDatabase
import com.amitranofinzi.vimata.ui.navigation.AppNav
import com.amitranofinzi.vimata.ui.theme.VimataTheme
import com.google.firebase.FirebaseApp





class MainActivity : BaseActivity() {
    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        appDatabase = AppDatabase.getDatabase(this)

        FirebaseApp.initializeApp(this)
        setContent {

            // Applying style theme on every UI component
            VimataTheme {
                AppNav(appDatabase, context = this)
            }

        }
    }
}

