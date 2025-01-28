package com.github.group06.chelas_reversi.screens.pairing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.screens.ErrorView

const val IDLE_VIEW_TAG = "idle_view"
const val SEARCHING_VIEW_TAG = "searching_view"
const val SEARCHING_CANCEL_VIEW_TAG = "searching_cancel_view"
const val FOUND_VIEW_TAG = "found_view"
const val ERROR_VIEW_TAG = "error_view"

@Composable
fun PairingScreen(
    viewModel: PairingViewModel,
    authorScreenIntent: () -> Unit,
    quitScreenIntent: () -> Unit,
    nickScreenIntent: () -> Unit,
    favoritesScreenIntent: () -> Unit,
    gameIntent: (String) -> Unit
) {

    when (val currentState = viewModel.state.collectAsState().value) {

        is PairingState.IdlePairingState -> PairingIdleView(
            modifier = Modifier.testTag(IDLE_VIEW_TAG),
            authorScreenIntent = authorScreenIntent,
            onStartSearchingClick = { viewModel.startSearching(gameIntent) },
            quitScreenIntent = quitScreenIntent,
            nickScreenIntent = nickScreenIntent,
            favoritesScreenIntent = favoritesScreenIntent
        )
        is PairingState.SearchingPairingState -> PairingSearchingView(
            modifier = Modifier.testTag(SEARCHING_VIEW_TAG),
            onCancelClick = { viewModel.cancelSearching() },
            players = currentState.players
        )
        is PairingState.SearchingCancelPairingState -> PairingCancelSearchingView(
            modifier = Modifier.testTag(SEARCHING_CANCEL_VIEW_TAG)
        )
        is PairingState.FoundPairingState -> PairingFoundView(
            modifier = Modifier.testTag(FOUND_VIEW_TAG),
            currentState.player
        )
        is PairingState.ErrorPairingState -> ErrorView(
            modifier = Modifier.testTag(ERROR_VIEW_TAG),
            msg = stringResource(
                when (currentState.error) {
                    PairingError.INVALID_NICK -> R.string.error_invalid_nick_text
                    PairingError.LISTENING_PLAYERS -> R.string.error_listening_player_text
                    PairingError.SEARCHING_PLAYER -> R.string.error_searching_player_text
                }
            )
        )

    }

}