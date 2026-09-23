package com.example.feature.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.R
import com.example.core.ui.composable.LoadingContent
import com.example.core.ui.theme.VitesseAppTheme

@Composable
fun DetailsCandidateScreen(
    viewModel: DetailsCandidateViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            if (uiState is DetailsCandidateUiState.Success) {
                AppBar(
                    onBackClick = onBackClick,
                    firstName = (uiState as DetailsCandidateUiState.Success).candidate.firstName,
                    lastName = (uiState as DetailsCandidateUiState.Success).candidate.lastName
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (uiState) {
                is DetailsCandidateUiState.Error -> {}
                is DetailsCandidateUiState.Success -> {}
                is DetailsCandidateUiState.Loading -> {
                    LoadingContent()
                }
            }
        }
    }
}

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    firstName: String,
    lastName: String
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = firstName + " " + lastName.uppercase())
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back_24dp),
                    contentDescription = stringResource(R.string.content_description_back)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview
@Composable
private fun AppBarPreview() {
    VitesseAppTheme {
        AppBar(
            onBackClick = {},
            firstName = "John",
            lastName = "Doe"
        )
    }
}