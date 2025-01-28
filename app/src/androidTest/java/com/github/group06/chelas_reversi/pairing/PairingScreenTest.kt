package com.github.group06.chelas_reversi.pairing

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.group06.chelas_reversi.DependenciesContainer
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.infrastructure.NickRepositoryImpl
import com.github.group06.chelas_reversi.screens.pairing.ERROR_VIEW_TAG
import com.github.group06.chelas_reversi.screens.pairing.PairingError
import com.github.group06.chelas_reversi.screens.pairing.FOUND_VIEW_TAG
import com.github.group06.chelas_reversi.screens.pairing.IDLE_VIEW_TAG
import com.github.group06.chelas_reversi.screens.pairing.PairingScreen
import com.github.group06.chelas_reversi.screens.pairing.PairingState
import com.github.group06.chelas_reversi.screens.pairing.PairingViewModel
import com.github.group06.chelas_reversi.screens.pairing.SEARCHING_CANCEL_VIEW_TAG
import com.github.group06.chelas_reversi.screens.pairing.SEARCHING_VIEW_TAG
import com.github.group06.chelas_reversi.services.FakePairingService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PairingScreenTest {

    @get:Rule
    val composeRuleTest = createComposeRule()

    private val dataStore by lazy {
        val dependenciesContainer = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as DependenciesContainer
        dependenciesContainer.preferencesDataStore
    }

    @Test
    fun verify_if_idle_view_is_shown() {

        composeRuleTest.setContent {
            PairingScreen(
                viewModel = PairingViewModel(
                    service = FakePairingService(),
                    nickRepository = NickRepositoryImpl(dataStore),
                ),
                quitScreenIntent = {},
                authorScreenIntent = {},
                nickScreenIntent = {},
                gameIntent = {},
                favoritesScreenIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(IDLE_VIEW_TAG).assertExists()
    }

    @Test
    fun verify_if_searching_view_is_shown() {

        composeRuleTest.setContent {
            PairingScreen(
                viewModel = PairingViewModel(
                    service = FakePairingService(),
                    nickRepository = NickRepositoryImpl(dataStore),
                    initialState = PairingState.SearchingPairingState(emptyList())
                ),
                quitScreenIntent = {},
                authorScreenIntent = {},
                nickScreenIntent = {},
                gameIntent = {},
                favoritesScreenIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(SEARCHING_VIEW_TAG).assertExists()
    }

    @Test
    fun verify_if_searching_cancel_view_is_shown() {

        composeRuleTest.setContent {
            PairingScreen(
                viewModel = PairingViewModel(
                    service = FakePairingService(),
                    nickRepository = NickRepositoryImpl(dataStore),
                    initialState = PairingState.SearchingCancelPairingState
                ),
                quitScreenIntent = {},
                authorScreenIntent = {},
                nickScreenIntent = {},
                gameIntent = {},
                favoritesScreenIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(SEARCHING_CANCEL_VIEW_TAG).assertExists()
    }

    @Test
    fun verify_if_found_view_is_shown() {

        composeRuleTest.setContent {
            PairingScreen(
                viewModel = PairingViewModel(
                    service = FakePairingService(),
                    nickRepository = NickRepositoryImpl(dataStore),
                    initialState = PairingState.FoundPairingState(Player(nickname = "Pedro"))
                ),
                quitScreenIntent = {},
                authorScreenIntent = {},
                nickScreenIntent = {},
                gameIntent = {},
                favoritesScreenIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(FOUND_VIEW_TAG).assertExists()
    }

    @Test
    fun verify_if_error_view_is_shown() {

        composeRuleTest.setContent {
            PairingScreen(
                viewModel = PairingViewModel(
                    service = FakePairingService(),
                    nickRepository = NickRepositoryImpl(dataStore),
                    initialState = PairingState.ErrorPairingState(PairingError.LISTENING_PLAYERS)
                ),
                quitScreenIntent = {},
                authorScreenIntent = {},
                nickScreenIntent = {},
                gameIntent = {},
                favoritesScreenIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(ERROR_VIEW_TAG).assertExists()
    }

}