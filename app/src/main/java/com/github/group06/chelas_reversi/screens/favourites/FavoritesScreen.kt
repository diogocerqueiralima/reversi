package com.github.group06.chelas_reversi.screens.favourites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.github.group06.chelas_reversi.R
import com.github.group06.chelas_reversi.screens.ErrorView
import com.github.group06.chelas_reversi.screens.WaitingView

const val LOADING_VIEW_TAG = "loading_view"
const val DELETING_VIEW_TAG = "deleting_view"
const val LISTENING_VIEW_TAG = "listening_view_tag"
const val REPLAY_VIEW_TAG = "replay_view"

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    navigateToPairing: () -> Unit,
) {

    when (val state = viewModel.state.collectAsState().value) {
        is FavoriteState.Loading -> WaitingView(
            modifier = Modifier.testTag(LOADING_VIEW_TAG),
            message = stringResource(R.string.favorites_loading)
        )
        is FavoriteState.Deleting -> WaitingView(
            modifier = Modifier.testTag(DELETING_VIEW_TAG),
            message = stringResource(R.string.deleting_game)
        )
        is FavoriteState.Listing -> FavoritesView(
            modifier = Modifier.testTag(LISTENING_VIEW_TAG),
            backIntent = navigateToPairing,
            games = state.games,
            removeFromFavoritesIntent = { game -> viewModel.removeFromFavorites(game) },
            startReplay = { game -> viewModel.startReplay(game) }
        )
        is FavoriteState.Replay -> ReplayView(
            modifier = Modifier.testTag(REPLAY_VIEW_TAG),
            game = state.game,
            play = state.play,
            previousPlay = { viewModel.previousPlay(state.game, state.index - 1) },
            nextPlay = { viewModel.nextPlay(state.game, state.index + 1) },
            backIntent = { viewModel.back() }
        )
    }

}