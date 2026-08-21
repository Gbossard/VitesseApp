package com.example.vitesseapp.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vitesseapp.R
import com.example.vitesseapp.data.local.CandidateEntity
import com.example.vitesseapp.ui.composable.LoadingContent
import com.example.vitesseapp.ui.theme.VitesseAppTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun HomeScreen(
    onFabClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            HomeSearch(
                onClearQuery = viewModel::onClearQuery,
                onQueryChange = viewModel::onQueryChange,
                query = searchQuery
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_24dp),
                    contentDescription = stringResource(R.string.add_candidate)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            HomeTabs(
                homeViewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearch(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState(initialText = query)
    val scope = rememberCoroutineScope()

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text }
            .collect { newQuery ->
                onQueryChange(newQuery.toString())
            }
    }
    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                textFieldState = textFieldState,
                searchBarState = searchBarState,
                onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
                placeholder = {
                    Text(
                        modifier = Modifier.clearAndSetSemantics {},
                        text = stringResource(R.string.search_bar)
                    )
                },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        IconButton(onClick = {
                            textFieldState.clearText()
                            onClearQuery()
                        }) {
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
            )
        }
    AppBarWithSearch(
        state = searchBarState,
        inputField = inputField,
        modifier = modifier,
    )
}

@Composable
fun HomeTabs(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    var state by rememberSaveable { mutableIntStateOf(0) }
    val uiState by when (state) {
        0 -> homeViewModel.candidatesUiState.collectAsStateWithLifecycle()
        else -> homeViewModel.favoritesUiState.collectAsStateWithLifecycle()
    }

    val titles = listOf(stringResource(R.string.tab_item_all), stringResource(R.string.tab_item_favorite))
    Column(
        modifier = modifier
    ) {
        PrimaryTabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    unselectedContentColor = Color.Unspecified,
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
                CandidatesList(candidates = (uiState as HomeUiState.Success).candidates)
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
    candidates: List<CandidateEntity>
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = candidates, key = {it.id}) { candidate ->
            CandidateItem(candidate = candidate)
        }
    }
}

@Composable
fun CandidateItem(
    modifier: Modifier = Modifier,
    candidate: CandidateEntity
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (candidate.photo == null) {
            Image(
                painter = painterResource(R.drawable.ic_empty_image_24dp),
                contentDescription = stringResource(R.string.content_description_empty_image),
                modifier = Modifier
                    .width(56.dp)
                    .height(56.dp)
                    .background(Color.LightGray),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(candidate.photo)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.content_description_photo),
                modifier = Modifier
                    .width(56.dp)
                    .height(56.dp)
            )
        }
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = candidate.firstName + " " + candidate.lastName.uppercase(),
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
            )
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
            onClearQuery = {},
            onQueryChange = {},
            query = ""
        )
    }
}