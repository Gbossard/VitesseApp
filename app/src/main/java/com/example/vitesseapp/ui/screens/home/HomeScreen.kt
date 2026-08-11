package com.example.vitesseapp.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vitesseapp.R
import com.example.vitesseapp.data.local.CandidateEntity
import com.example.vitesseapp.ui.theme.VitesseAppTheme
import java.time.LocalDate

@Composable
fun HomeScreen() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            // TODO search composable
            HomeTabs()
        }
    }
}

@Composable
fun HomeTabs(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    var state by rememberSaveable { mutableIntStateOf(0) }
    val titles = listOf(stringResource(R.string.tab_item_all), "Tab 2")
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
        if (state == 0 ) {
            when(homeUiState) {
                is HomeUiState.Empty -> {}
                is HomeUiState.Error -> {}
                is HomeUiState.Success -> {
                    CandidatesList(candidates = (homeUiState as HomeUiState.Success).candidates)
                }
                is HomeUiState.Loading -> {}
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
        Image(
            painter = painterResource(R.drawable.ic_empty_image_24dp),
            contentDescription = stringResource(R.string.content_description_empty_image),
            modifier = Modifier
                .width(56.dp)
                .height(56.dp)
                .background(Color.LightGray),
            colorFilter = ColorFilter.tint(Color.Gray)
        )
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
                photo = "",
                salary = 100,
                notes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            )
        )
    }
}