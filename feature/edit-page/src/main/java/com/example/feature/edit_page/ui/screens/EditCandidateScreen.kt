package com.example.feature.edit_page.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.photopicker.compose.ExperimentalPhotoPickerComposeApi
import coil3.compose.AsyncImage
import com.example.core.ui.theme.VitesseAppTheme
import com.example.feature.edit_page.R
import com.example.feature.edit_page.ui.composable.EmbeddedPhotoPickerModalBottomSheet
import com.example.feature.edit_page.ui.composable.isEmbeddedPhotoPickerSupported
import com.example.feature.edit_page.ui.composable.toLocalDate
import kotlinx.coroutines.launch

@SuppressLint("LocalContextResourcesRead")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditCandidateScreen(
    viewModel: EditCandidateViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val editUiState by viewModel.editUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is EditUiEvent.SaveSuccess -> {
                    onSaveClick()
                }
                is EditUiEvent.SaveErrorEvent -> {
                    val resourcesId = when(event.error) {
                        is SaveError.DatabaseError -> R.string.form_error_save_database
                        is SaveError.FileNotFound -> R.string.form_error_save_file_not_found
                        is SaveError.StorageFull -> R.string.form_error_save_storage_full
                        is SaveError.Unknown -> R.string.form_error_save_unknown
                    }
                    snackbarHostState.showSnackbar(message = context.resources.getString(resourcesId))
                }
            }
        }
    }
    Scaffold(
        topBar = {
            AppBar(onBackClick = onBackClick)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = {
                    viewModel.saveCandidate()
                }
            ) {
                Text(
                    text = stringResource(R.string.form_save)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            PhotoSection(
                photoUri = editUiState.photo,
                onPhotoChange = viewModel::onPhotoChange
            )
            NameSection(
                firstName = editUiState.firstName,
                lastName = editUiState.lastName
            )
            InformationSection(
                painterRes = com.example.core.R.drawable.ic_call_24dp,
                painterDescription = com.example.core.R.string.content_description_phone_number,
                label = com.example.core.R.string.form_phone_number,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                state = editUiState.phone
            )
            InformationSection(
                painterRes = com.example.core.R.drawable.ic_mail_24dp,
                painterDescription = com.example.core.R.string.content_description_email,
                label = com.example.core.R.string.form_email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                state = editUiState.email
            )
            DateSection(
                onDateOfBirthChange = { millis ->
                    viewModel.onDateOfBirthChange(millis?.toLocalDate())

                }
            )
            InformationSection(
                painterRes = R.drawable.ic_attach_money_24dp,
                painterDescription = com.example.core.R.string.content_description_salary,
                label = com.example.core.R.string.form_salary,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                state = editUiState.salary
            )
            InformationSection(
                modifier = Modifier.padding(bottom = 88.dp),
                painterRes = com.example.core.R.drawable.ic_edit_24dp,
                painterDescription = R.string.content_description_notes,
                label = com.example.core.R.string.form_notes,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Unspecified),
                state = editUiState.notes,
                lineLimits = TextFieldLineLimits.MultiLine(8)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPhotoPickerComposeApi::class)
@Composable
fun PhotoSection(
    photoUri: Uri?,
    onPhotoChange: (Uri?) -> Unit
) {
    var attachments by rememberSaveable { mutableStateOf(listOfNotNull(photoUri)) }
    val currentPhoto = attachments.singleOrNull()

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden
    )

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            attachments = listOf(uri)
            onPhotoChange(attachments.singleOrNull())
        }
    }

    val onPhotoPickerClick = {
        if (isEmbeddedPhotoPickerSupported()) {
            coroutineScope.launch {
                bottomSheetState.partialExpand()
            }
        } else {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .aspectRatio(2f / 1f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (currentPhoto == null) {
            Image(
                painter = painterResource(com.example.core.R.drawable.ic_empty_image_24dp),
                contentDescription = stringResource(com.example.core.R.string.content_description_empty_image),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(32.dp))
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.LightGray)
                    .clickable(onClick = { onPhotoPickerClick() }),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        } else {
            AsyncImage(
                model = currentPhoto,
                contentDescription = stringResource(com.example.core.R.string.content_description_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(32.dp))
                    .clickable(onClick = { onPhotoPickerClick() })
            )
        }
    }

    if (isEmbeddedPhotoPickerSupported() && bottomSheetState.isVisible) {
        EmbeddedPhotoPickerModalBottomSheet(
            sheetState = bottomSheetState,
            onUriPermissionGranted = { uris ->
                attachments += uris
                onPhotoChange(attachments.singleOrNull())
            },
            onUriPermissionRevoked = { uris ->
                attachments -= uris
                onPhotoChange(attachments.singleOrNull())
            }
        )
    }
}

@Composable
fun NameSection(
    modifier: Modifier = Modifier,
    firstName: TextFieldState,
    lastName: TextFieldState
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier.padding(top = 8.dp, end = 16.dp),
            painter = painterResource(R.drawable.ic_person_24dp),
            contentDescription = stringResource(R.string.content_description_person_name)
        )
        Column {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                state = firstName,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.form_first_name)) }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                state = lastName,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.form_last_name)) }
            )
        }
    }
}

@Composable
fun InformationSection(
    modifier: Modifier = Modifier,
    painterRes: Int,
    painterDescription: Int,
    label: Int,
    keyboardOptions: KeyboardOptions,
    state: TextFieldState,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier.padding(top = 8.dp, end = 16.dp),
            painter = painterResource(painterRes),
            contentDescription = stringResource(painterDescription)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            lineLimits = lineLimits,
            keyboardOptions = keyboardOptions,
            label = { Text(stringResource(label)) }
        )
    }
}

@Composable
fun DateSection(
    modifier: Modifier = Modifier,
    onDateOfBirthChange: (Long?) -> Unit
) {
    val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    LaunchedEffect(state.selectedDateMillis) {
        onDateOfBirthChange(state.selectedDateMillis)
    }

    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier.padding(top = 8.dp, end = 16.dp),
            painter = painterResource(R.drawable.ic_cake_24dp),
            contentDescription = stringResource(R.string.content_description_date_of_birth)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DatePicker(state = state, focusRequester = null, modifier = Modifier.clip(shape = RoundedCornerShape(32.dp)))
        }
    }

}

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(com.example.core.R.string.add_candidate))
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(com.example.core.R.drawable.ic_arrow_back_24dp),
                    contentDescription = stringResource(com.example.core.R.string.content_description_back)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreview() {
    VitesseAppTheme {
        AppBar(
            onBackClick = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun EditCandidateScreenPreview() {
    VitesseAppTheme {
        EditCandidateScreen(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NameSectionPreview() {
    VitesseAppTheme {
        NameSection(
            firstName = rememberTextFieldState("Jean"),
            lastName = rememberTextFieldState("Dupont")
        )
    }
}