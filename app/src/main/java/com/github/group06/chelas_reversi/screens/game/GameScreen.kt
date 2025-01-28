package com.github.group06.chelas_reversi.screens.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.screens.ErrorView
import com.github.group06.chelas_reversi.screens.WaitingView

const val LOADING_GAME_VIEW_TAG = "loading_game_view"
const val WAITING_VIEW_TAG = "waiting_game_view"
const val CHOOSING_COLOR_GAME_VIEW_TAG = "choosing_color_game_view"
const val PLAYING_GAME_VIEW_TAG = "playing_game_view"
const val WAITING_GAME_VIEW_TAG = "waiting_game_view"
const val GAME_FINISHED_VIEW_TAG = "game_finished_view"
const val ERROR_GAME_VIEW_TAG = "error_game_view"

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    navigateToPairingIntent: () -> Unit
){

    when(val currentState = viewModel.state.collectAsState().value){

        is GameState.Loading -> LoadingGameView(
            modifier = Modifier.testTag(LOADING_GAME_VIEW_TAG)
        )

        is GameState.Saving -> WaitingView(
            modifier = Modifier.testTag(WAITING_VIEW_TAG),
            message = stringResource(R.string.saving_game)
        )

        is GameState.ChooseColorState -> ChoosingColorView(
            modifier = Modifier.testTag(CHOOSING_COLOR_GAME_VIEW_TAG),
            player = currentState.game.me,
            timeout = currentState.timer.value,
            opponent = currentState.game.opponent,
            onChooseColorIntent = { piece -> viewModel.chooseColor(piece, currentState.game) }
        )

        is GameState.PlayingState -> PlayingGameView(
            modifier = Modifier.testTag(PLAYING_GAME_VIEW_TAG),
            game = currentState.game,
            clickable = { slot -> viewModel.chooseSlot(currentState.game, slot) }
        )

        is GameState.AlreadyPlayedState -> PlayingGameView(
            modifier = Modifier.testTag(PLAYING_GAME_VIEW_TAG),
            game = currentState.game,
            clickable = {}
        )

        is GameState.WaitingState -> WaitingGameView(
            modifier = Modifier.testTag(WAITING_GAME_VIEW_TAG),
            game = currentState.game
        )

        is GameState.FinishedState -> GameFinishedView(
            modifier = Modifier.testTag(GAME_FINISHED_VIEW_TAG),
            game = currentState.game,
            navigateToPairingIntent = navigateToPairingIntent,
            addToFavoriteIntent = { game -> viewModel.addGameToFavorite(game, navigateToPairingIntent) }
        )

        is GameState.ErrorGameState -> ErrorView(
            modifier = Modifier.testTag(ERROR_GAME_VIEW_TAG),
            msg = stringResource(
                when (currentState.error) {
                    GameError.INVALID_NICK -> R.string.error_invalid_nick_text
                    GameError.AWAIT_COLOR -> R.string.await_color_error
                }
            )
        )

    }

}