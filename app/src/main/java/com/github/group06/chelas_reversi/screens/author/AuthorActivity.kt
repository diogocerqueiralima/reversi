package com.github.group06.chelas_reversi.screens.author

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.group06.chelas_reversi.ui.theme.ChelasreversiTheme

class AuthorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChelasreversiTheme {
                AuthorScreen(emailIntent = { authors ->

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_EMAIL, authors.map { it.email }.toTypedArray())
                    }

                    startActivity(intent)
                }, backIntent = { finish() })
            }
        }
    }
}