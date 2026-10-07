package com.example.feature.home.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.core.data.local.CandidateEntity
import com.example.core.ui.composable.LoadingContent
import com.example.core.ui.theme.VitesseAppTheme
import com.example.feature.home.R
import java.time.LocalDate

@Composable
fun HomeScreen(
    onFabClick: () -> Unit,
    onCandidateClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val candidatesUiState by viewModel.candidatesUiState.collectAsStateWithLifecycle()
    val favoritesUiState by viewModel.favoritesUiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            HomeSearch(
                onQueryChange = viewModel::onQueryChange,
                query = searchQuery
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_24dp),
                    contentDescription = stringResource(com.example.core.R.string.add_candidate)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            HomeTabs(
                candidatesUiState = candidatesUiState,
                favoritesUiState = favoritesUiState,
                onCandidateClick = onCandidateClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearch(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    val textFieldState = rememberTextFieldState(initialText = query)
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text }
            .collect { newQuery ->
                onQueryChange(newQuery.toString())
            }
    }

    TextField(
        state = textFieldState,
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        lineLimits = TextFieldLineLimits.SingleLine,
        shape = CircleShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(
                modifier = Modifier.clearAndSetSemantics {},
                text = stringResource(R.string.search_bar)
            )
        },
        trailingIcon = {
            if (textFieldState.text.isNotEmpty()) {
                IconButton(onClick = { textFieldState.clearText() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_24dp),
                        contentDescription = stringResource(R.string.content_description_delete_query)
                    )
                }
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_search_24dp),
                    contentDescription = stringResource(R.string.search_bar)
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onKeyboardAction = { keyboardController?.hide() },
    )
}

@Composable
fun HomeTabs(
    modifier: Modifier = Modifier,
    candidatesUiState: HomeUiState,
    favoritesUiState: HomeUiState,
    onCandidateClick: (String) -> Unit,
) {
    var state by rememberSaveable { mutableIntStateOf(0) }
    val uiState = when (state) {
        0 -> candidatesUiState
        else -> favoritesUiState
    }

    val titles = listOf(stringResource(R.string.tab_item_all), stringResource(R.string.tab_item_favorite))
    Column(
        modifier = modifier
    ) {
        PrimaryTabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = { state = index },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                )
            }
        }
        when (uiState) {
            is HomeUiState.Empty -> {
                EmptyContent()
            }
            is HomeUiState.Error -> {}
            is HomeUiState.Success -> {
                CandidatesList(candidates = uiState.candidates, onCandidateClick = onCandidateClick)
            }
            is HomeUiState.Loading -> {
                LoadingContent()
            }
        }
    }
}

@Composable
fun CandidatesList(
    modifier: Modifier = Modifier,
    candidates: List<CandidateEntity>,
    onCandidateClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = candidates, key = {it.id}) { candidate ->
            CandidateItem(candidate = candidate, onCandidateClick = onCandidateClick)
        }
    }
}

@Composable
fun CandidateItem(
    modifier: Modifier = Modifier,
    candidate: CandidateEntity,
    onCandidateClick: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .clickable(onClick = { onCandidateClick(candidate.id) })
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (candidate.photo == null) {
                Image(
                    painter = painterResource(com.example.core.R.drawable.ic_empty_image_24dp),
                    contentDescription = stringResource(com.example.core.R.string.content_description_empty_image),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
            } else {
                AsyncImage(
                    model = candidate.photo,
                    contentDescription = stringResource(com.example.core.R.string.content_description_photo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "${candidate.firstName} ${candidate.lastName.uppercase()}",
                fontWeight = FontWeight.Medium
            )
            Text(
                text = candidate.notes,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun EmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_list_candidate),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CandidateItemPreview() {
    VitesseAppTheme {
        CandidateItem(
            candidate = CandidateEntity(
                id = "1",
                firstName = "John",
                lastName = "Doe",
                phone = "+330606060606",
                email = "johndoe@gmail.com",
                dateOfBirth = LocalDate.of(2026, 7, 21),
                photo = null,
                salary = 100,
                notes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            ),
            onCandidateClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyContentPreview() {
    VitesseAppTheme {
        EmptyContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeSearchPreview() {
    VitesseAppTheme {
        HomeSearch(
            onQueryChange = {},
            query = ""
        )
    }
}