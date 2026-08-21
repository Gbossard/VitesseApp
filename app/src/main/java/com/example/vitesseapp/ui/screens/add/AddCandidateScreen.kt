package com.example.vitesseapp.ui.screens.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vitesseapp.R
import com.example.vitesseapp.ui.theme.VitesseAppTheme

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
            NameSection()
        }
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
        AddCandidateScreen {  }
    }
}

@Preview(showBackground = true)
@Composable
private fun NameSectionPreview() {
    VitesseAppTheme {
        NameSection()
    }
}