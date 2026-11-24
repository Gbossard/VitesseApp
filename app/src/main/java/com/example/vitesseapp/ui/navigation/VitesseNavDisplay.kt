package com.example.vitesseapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.feature.details.ui.screens.DetailsCandidateRoute
import com.example.feature.edit_page.ui.screens.EditCandidateRoute
import com.example.feature.home.ui.HomeRoute

@Composable
fun VitesseNavDisplay(
    backStack: NavBackStack<NavKey> = rememberNavBackStack(Home)
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators =
        listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider =
        entryProvider {
            entry<Home> {
                HomeRoute(
                    onFabClick = {
                        backStack.add(EditCandidate())
                    },
                    onCandidateClick = { candidateId ->
                        backStack.add(DetailsCandidate(candidateId = candidateId))
                    }
                )
            }
            entry<EditCandidate> { key ->
                EditCandidateRoute(
                    candidateId = key.candidateId,
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                    onSaveClick = {
                        backStack.clear()
                        backStack.add(Home)
                    }
                )
            }
            entry<DetailsCandidate> { key ->
                DetailsCandidateRoute(
                    candidateId = key.candidateId,
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                    onEditClick = { candidateId ->
                        backStack.add(EditCandidate(candidateId = candidateId))
                    }
                )
            }
        }
    )
}
