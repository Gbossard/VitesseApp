package com.example.feature.details.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.R
import com.example.core.data.local.CandidateEntity
import com.example.core.ui.composable.LoadingContent
import com.example.core.ui.composable.PhotoContent
import com.example.core.ui.theme.VitesseAppTheme
import com.example.feature.details.ui.util.dialPhoneNumber
import com.example.feature.details.ui.util.openEmail
import com.example.feature.details.ui.util.openSms
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun DetailsCandidateScreen(
    viewModel: DetailsCandidateViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val openDialog = rememberSaveable { mutableStateOf(false) }
    val state = uiState

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        when (state) {
            is DetailsCandidateUiState.Error -> {}
            is DetailsCandidateUiState.Success -> {
                DetailsContent(
                    modifier = Modifier.padding(innerPadding),
                    candidate = state.candidate,
                    onErrorContact = { message ->
                        scope.launch { snackbarHostState.showSnackbar(message) }
                    }
                )
            }

            is DetailsCandidateUiState.Loading -> {
                LoadingContent()
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
fun DetailsContent(
    modifier: Modifier = Modifier,
    candidate: CandidateEntity,
    onErrorContact: (String) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        PhotoContent(
            photo = candidate.photo
        )
        ContactContent(
            phone = candidate.phone,
            email = candidate.email,
            onError = onErrorContact
        )
        InformationContent(
            dateOfBirth = candidate.dateOfBirth,
            notes = candidate.notes,
            salary = candidate.salary
        )
    }
}

@Composable
fun ContactContent(
    modifier: Modifier = Modifier,
    phone: String,
    email: String,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContactItem(
            iconRes = R.drawable.ic_call_24dp,
            textRes = com.example.feature.details.R.string.call_button,
            onClick = {context.dialPhoneNumber(phone, onError = onError)}
        )
        ContactItem(
            iconRes = com.example.feature.details.R.drawable.ic_chat_24dp,
            textRes = com.example.feature.details.R.string.sms_button,
            onClick = {context.openSms(phone, onError = onError)}
        )
        ContactItem(
            iconRes = R.drawable.ic_mail_24dp,
            textRes = com.example.feature.details.R.string.email_button,
            onClick = {context.openEmail(email, onError = onError)}
        )
    }
}

@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    iconRes: Int,
    textRes: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = stringResource(textRes)
            )
        }
        Text(
            text = stringResource(textRes),
            fontSize = 12.sp,
        )
    }
}


@Composable
fun InformationContent(
    modifier: Modifier = Modifier,
    dateOfBirth: LocalDate,
    notes: String,
    salary: Int
) {
    val dateFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.SHORT)
        .withLocale(LocalLocale.current.platformLocale)
    val formattedDate = dateOfBirth.format(dateFormatter)
    val age = Period.between(dateOfBirth, LocalDate.now()).years

    Column(modifier = modifier.padding(16.dp)) {
        CardContent(
            title = stringResource(com.example.feature.details.R.string.headline_about),
            subhead = "$formattedDate ${stringResource(id = com.example.feature.details.R.string.subhead_years, age)}",
            body = stringResource(com.example.feature.details.R.string.body_birthday)
        )
        CardContent(
            title = stringResource(com.example.feature.details.R.string.headline_expected_salary),
            subhead = stringResource(id = com.example.feature.details.R.string.subhead_euros, salary),
            body = stringResource(com.example.feature.details.R.string.body_pounds, salary)
        )
        CardContent(
            title = stringResource(com.example.feature.details.R.string.headline_notes),
            body = notes
        )
    }
}

@Composable
fun CardContent(
    modifier: Modifier = Modifier,
    title: String,
    subhead: String? = null,
    body: String
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        if (subhead != null) {
            Text(
                text = subhead,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
        }
        Text(
            text = body,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
        )
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
            Text(text = "$firstName ${lastName.uppercase()}")
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
private fun PhotoContentPreview() {
    VitesseAppTheme {
        PhotoContent(
            photo = null
        )
    }
}

@Preview
@Composable
private fun ContactContentPreview() {
    VitesseAppTheme {
        InformationContent(
            dateOfBirth = LocalDate.of(2026, 7, 21),
            notes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            salary = 50000
        )
    }
}


@Preview
@Composable
private fun CardContentPreview() {
    VitesseAppTheme {
        CardContent(
            title = "Title",
            subhead = "Subhead",
            body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        )
    }
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