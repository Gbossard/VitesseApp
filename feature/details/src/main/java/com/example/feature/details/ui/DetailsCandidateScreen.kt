package com.example.feature.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
    onEditClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val openDialog = rememberSaveable { mutableStateOf(false) }
    val state = uiState
    Scaffold(
        topBar = {

            if (state is DetailsCandidateUiState.Success) {
                val candidate = state.candidate
                AppBar(
                    onBackClick = onBackClick,
                    onFavoriteClick = { viewModel.toggleFavorite() },
                    onEditClick = { onEditClick(candidate.id) },
                    onDeleteClick = { openDialog.value = !openDialog.value },
                    firstName = candidate.firstName,
                    lastName = candidate.lastName,
                    isFavorite = candidate.isFavorite,
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

        if (openDialog.value && state is DetailsCandidateUiState.Success) {
            DeleteDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                onConfirmation = {
                    viewModel.deleteCandidate(state.candidate)
                    onBackClick()
                }
            )
        }
    }
}

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    firstName: String,
    lastName: String,
    isFavorite: Boolean,
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
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    painter = if (isFavorite) painterResource(com.example.feature.details.R.drawable.ic_star_fill_24dp) else painterResource(com.example.feature.details.R.drawable.ic_star_24dp),
                    contentDescription = stringResource(com.example.feature.details.R.string.content_description_favorites),
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    painter =  painterResource(R.drawable.ic_edit_24dp),
                    contentDescription = stringResource(R.string.content_description_edit),
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    painter =  painterResource(com.example.feature.details.R.drawable.ic_delete_24dp),
                    contentDescription = stringResource(com.example.feature.details.R.string.content_description_delete),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = stringResource(com.example.feature.details.R.string.deletion))
        },
        text = {
            Text(text = stringResource(com.example.feature.details.R.string.dialog_text_delete))
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text(text = stringResource(com.example.feature.details.R.string.dialog_button_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(com.example.feature.details.R.string.dialog_button_cancel))

            }
        }
    )
}

@Preview
@Composable
private fun AppBarPreview() {
    VitesseAppTheme {
        AppBar(
            onBackClick = {},
            onFavoriteClick = {},
            onEditClick = {},
            onDeleteClick = {},
            firstName = "John",
            lastName = "Doe",
            isFavorite = true,
        )
    }
}