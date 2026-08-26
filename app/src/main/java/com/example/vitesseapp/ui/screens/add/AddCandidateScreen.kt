package com.example.vitesseapp.ui.screens.add

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.photopicker.compose.ExperimentalPhotoPickerComposeApi
import coil3.compose.AsyncImage
import com.example.vitesseapp.R
import com.example.vitesseapp.ui.composable.EmbeddedPhotoPickerModalBottomSheet
import com.example.vitesseapp.ui.composable.isEmbeddedPhotoPickerSupported
import com.example.vitesseapp.ui.theme.VitesseAppTheme
import kotlinx.coroutines.launch

@Composable
fun AddCandidateScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppBar(onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            PhotoSection()
            NameSection()
            InformationSection(
                painterRes = R.drawable.ic_call_24dp,
                painterDescription = R.string.content_description_phone_number,
                label = R.string.form_phone_number,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                state = rememberTextFieldState()
            )
            InformationSection(
                painterRes = R.drawable.ic_mail_24dp,
                painterDescription = R.string.content_description_email,
                label = R.string.form_email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                state = rememberTextFieldState()
            )
            DateSection()
            InformationSection(
                painterRes = R.drawable.ic_attach_money_24dp,
                painterDescription = R.string.content_description_salary,
                label = R.string.form_salary,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                state = rememberTextFieldState()
            )
            InformationSection(
                painterRes = R.drawable.ic_edit_24dp,
                painterDescription = R.string.content_description_notes,
                label = R.string.form_notes,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Unspecified),
                state = rememberTextFieldState(),
                lineLimits = TextFieldLineLimits.MultiLine(8)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPhotoPickerComposeApi::class)
@Composable
fun PhotoSection() {
    var attachment by rememberSaveable { mutableStateOf<Uri?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden
    )

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) attachment = uri
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
        if (attachment == null) {
            Image(
                painter = painterResource(R.drawable.ic_empty_image_24dp),
                contentDescription = stringResource(R.string.content_description_empty_image),
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
                model = attachment,
                contentDescription = stringResource(R.string.content_description_photo),
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
                attachment = uris.firstOrNull()
            },
            onUriPermissionRevoked = { uris ->
                if (uris.contains(attachment)) attachment = null
            }
        )
    }
}

@Composable
fun NameSection(modifier: Modifier = Modifier) {
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
                state = rememberTextFieldState(),
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.form_first_name)) }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                state = rememberTextFieldState(),
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
fun DateSection(modifier: Modifier = Modifier) {
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
            val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
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
            Text(text = stringResource(R.string.add_candidate))
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

@Preview(showBackground = true)
@Composable
private fun AppBarPreview() {
    VitesseAppTheme {
        AppBar(
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddCandidateScreenPreview() {
    VitesseAppTheme {
        AddCandidateScreen {}
    }
}

@Preview(showBackground = true)
@Composable
private fun NameSectionPreview() {
    VitesseAppTheme {
        NameSection()
    }
}