package com.example.vitesseapp.ui.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.vitesseapp.ui.theme.VitesseAppTheme

@Composable
fun HomeScreen() {
    Text("HomeScreen")
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    VitesseAppTheme {
        HomeScreen()
    }
}