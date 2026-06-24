package com.example.randomquotegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.randomquotegenerator.ui.MainNavigation
import com.example.randomquotegenerator.ui.theme.RandomQuoteGeneratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomQuoteGeneratorTheme {
                MainNavigation()
            }
        }
    }
}
