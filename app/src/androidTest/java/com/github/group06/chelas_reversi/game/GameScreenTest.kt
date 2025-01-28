package com.github.group06.chelas_reversi.game

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.domain.Timer
import com.github.group06.chelas_reversi.infrastructure.NickRepositoryImpl
import com.github.group06.chelas_reversi.screens.game.CHOOSING_COLOR_GAME_VIEW_TAG
import com.github.group06.chelas_reversi.screens.game.GAME_FINISHED_VIEW_TAG
import com.github.group06.chelas_reversi.screens.game.GameScreen
import com.github.group06.chelas_reversi.screens.game.GameState
import com.github.group06.chelas_reversi.screens.game.GameViewModel
import com.github.group06.chelas_reversi.screens.game.LOADING_GAME_VIEW_TAG
import com.github.group06.chelas_reversi.screens.game.PLAYING_GAME_VIEW_TAG
import com.github.group06.chelas_reversi.screens.game.WAITING_VIEW_TAG
import com.github.group06.chelas_reversi.services.FakeGameService
import com.github.group06.chelas_reversi.utils.CleanDataStoreRule
import org.junit.Rule
import org.junit.Test

class GameScreenTest {

    @get:Rule
    val composeRuleTest = createComposeRule()

    @get:Rule
    val cleanDataStoreRule = CleanDataStoreRule()

    @Test
    fun loading_view_is_shown() {

        composeRuleTest.setContent {
            GameScreen(
                viewModel = GameViewModel(
                    gameService = FakeGameService(),
                    nickRepository = NickRepositoryImpl(cleanDataStoreRule.dataStore),
                    initialState = GameState.Loading
                ),
                navigateToPairingIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(LOADING_GAME_VIEW_TAG).assertExists()

    }

    @Test
    fun waiting_view_is_shown() {

        composeRuleTest.setContent {
            GameScreen(
                viewModel = GameViewModel(
                    gameService = FakeGameService(),
                    nickRepository = NickRepositoryImpl(cleanDataStoreRule.dataStore),
                    initialState = GameState.WaitingState(Game(me = Player("Mihail"), opponent = Player("Diogo")))
                ),
                navigateToPairingIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(WAITING_VIEW_TAG).assertExists()

    }

    @Test
    fun playing_view_is_shown() {

        composeRuleTest.setContent {
            GameScreen(
                viewModel = GameViewModel(
                    gameService = FakeGameService(),
                    nickRepository = NickRepositoryImpl(cleanDataStoreRule.dataStore),
                    initialState = GameState.PlayingState(Game(me = Player("Mihail"), opponent = Player("Diogo")))
                ),
                navigateToPairingIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(PLAYING_GAME_VIEW_TAG).assertExists()

    }

    @Test
    fun finished_view_is_shown() {

        composeRuleTest.setContent {
            GameScreen(
                viewModel = GameViewModel(
                    gameService = FakeGameService(),
                    nickRepository = NickRepositoryImpl(cleanDataStoreRule.dataStore),
                    initialState = GameState.FinishedState(Game(me = Player("Mihail"), opponent = Player("Diogo")))
                ),
                navigateToPairingIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(GAME_FINISHED_VIEW_TAG).assertExists()

    }

    @Test
    fun choose_color_view_is_shown() {

        composeRuleTest.setContent {
            GameScreen(
                viewModel = GameViewModel(
                    gameService = FakeGameService(),
                    nickRepository = NickRepositoryImpl(cleanDataStoreRule.dataStore),
                    initialState = GameState.ChooseColorState(Game(me = Player("Mihail"), opponent = Player("Diogo")), timer = Timer(10))
                ),
                navigateToPairingIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(CHOOSING_COLOR_GAME_VIEW_TAG).assertExists()

    }

}