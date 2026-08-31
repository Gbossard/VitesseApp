package com.example.core.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.ui.theme.VitesseAppTheme

@OptIn( ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        LoadingIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingContentPreview() {
    VitesseAppTheme {
        LoadingContent()
    }
}