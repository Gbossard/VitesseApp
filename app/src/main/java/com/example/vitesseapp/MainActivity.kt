package com.example.vitesseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vitesseapp.ui.navigation.VitesseNavHost
import com.example.vitesseapp.ui.theme.VitesseAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VitesseAppTheme {
                VitesseNavHost()
            }
        }
    }
}