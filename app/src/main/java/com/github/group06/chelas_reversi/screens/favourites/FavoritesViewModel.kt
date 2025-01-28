package com.github.group06.chelas_reversi.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Play
import com.github.group06.chelas_reversi.services.FavoritesService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface FavoriteState {

    data object Loading : FavoriteState
    data object Deleting : FavoriteState
    data class Listing(val games: List<Game>) : FavoriteState
    data class Replay(val game: Game, val play: Play, val index: Int) : FavoriteState

}

class FavoritesViewModel(

    private val service: FavoritesService,
    initialState: FavoriteState = FavoriteState.Loading

) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<FavoriteState> = _state.asStateFlow()

    fun loadGames(): Job? {

        if (_state.value !is FavoriteState.Loading)
            return null

        return viewModelScope.launch {

            delay(1000L)
            val games = service.loadGames()

            _state.value = FavoriteState.Listing(games)
        }

    }

    fun removeFromFavorites(game: Game): Job? {

        if (_state.value !is FavoriteState.Listing)
            return null

        _state.value = FavoriteState.Deleting

        return viewModelScope.launch {

            service.removeFromFavorites(game)
            val games = service.loadGames()

            _state.value = FavoriteState.Listing(games)
        }

    }

    fun startReplay(game: Game) {

        if (_state.value !is FavoriteState.Listing)
            return

        _state.value = FavoriteState.Replay(game, game.plays.first(), 0)
    }

    fun nextPlay(game: Game, index: Int) {

        if (_state.value !is FavoriteState.Replay || index >= game.plays.size || index < 0)
            return

        val play = game.plays[index]
        _state.value = FavoriteState.Replay(game, play, index)
    }

    fun previousPlay(game: Game, index: Int) {

        if (_state.value !is FavoriteState.Replay || index >= game.plays.size || index < 0)
            return

        val play = game.plays[index]
        _state.value = FavoriteState.Replay(game, play, index)
    }

    fun back(): Job? {

        if (_state.value !is FavoriteState.Replay)
            return null

        _state.value = FavoriteState.Loading
        return loadGames()

    }

}

@Suppress("UNCHECKED_CAST")
class FavoritesViewModelFactory(

    private val service: FavoritesService

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoritesViewModel(service) as T
    }
}