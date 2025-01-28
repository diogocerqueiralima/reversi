package com.github.group06.chelas_reversi.screens.pairing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.exceptions.StopListeningPlayersException
import com.github.group06.chelas_reversi.infrastructure.NickRepository
import com.github.group06.chelas_reversi.services.PairingService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

sealed interface PairingState {

    data object SearchingCancelPairingState : PairingState
    data object IdlePairingState : PairingState
    data class ErrorPairingState(val error: PairingError) : PairingState
    data class FoundPairingState(val player: Player) : PairingState
    data class SearchingPairingState(val players: List<Player>) : PairingState

}

enum class PairingError {

    INVALID_NICK,
    SEARCHING_PLAYER,
    LISTENING_PLAYERS

}

class PairingViewModel(

    private val service: PairingService,
    private val nickRepository: NickRepository,
    initialState: PairingState = PairingState.IdlePairingState

) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<PairingState> = _state.asStateFlow()

    private val timeoutAwaitGame = 1000L * 30
    private val searchingCancelDelay = 1000L * 5

    private var listeningPlayersJob: Job? = null
    var searchPlayersJob: Job? = null

    fun startSearching(gameIntent: (String) -> Unit) {

        if (_state.value !is PairingState.IdlePairingState)
            return

        startListeningPlayers()

        searchPlayersJob = viewModelScope.launch {

            val nick = nickRepository.getNick()

            if (nick == null) {
                listeningPlayersJob?.cancel(StopListeningPlayersException())
                _state.value = PairingState.ErrorPairingState(PairingError.INVALID_NICK)
                delay(searchingCancelDelay)
                _state.value = PairingState.IdlePairingState
                return@launch
            }

            val me = Player(nick = nick)
            val game = service.searchGame(me).first()

            leaveQueue(me)

            _state.value = PairingState.FoundPairingState(game.opponent)
            delay(2000L)

            try {

                withTimeout(timeoutAwaitGame) {

                    service.awaitGame(game).collectLatest {

                        if (it)
                            gameIntent(game.id)

                    }

                }

            }catch (e: Exception) {
                _state.value = PairingState.ErrorPairingState(PairingError.SEARCHING_PLAYER)
                delay(searchingCancelDelay)
                service.deleteGame(game)
                _state.value = PairingState.IdlePairingState
            }

        }

    }

    fun cancelSearching() {

        if (_state.value !is PairingState.SearchingPairingState) return

        viewModelScope.launch {

            val nick = nickRepository.getNick()!!
            val me = Player(nick = nick)
            leaveQueue(me)

            _state.value = PairingState.SearchingCancelPairingState
            delay(searchingCancelDelay)
            _state.value = PairingState.IdlePairingState
        }

    }

    private fun startListeningPlayers() {

        if (_state.value !is PairingState.IdlePairingState)
            return

        listeningPlayersJob = viewModelScope.launch {

            try {

                service.listenPlayers().collectLatest { players ->
                    _state.value = PairingState.SearchingPairingState(players)
                }

            }catch (_: StopListeningPlayersException) {
            }catch (e: Exception) {
                _state.value = PairingState.ErrorPairingState(PairingError.LISTENING_PLAYERS)
                delay(searchingCancelDelay)
                _state.value = PairingState.IdlePairingState
            }

        }

    }

    private suspend fun leaveQueue(me: Player) {
        service.leaveQueue(me)
        listeningPlayersJob?.cancel(StopListeningPlayersException())
    }

}

@Suppress("UNCHECKED_CAST")
class PairingViewModelFactory(

    private val service: PairingService,
    private val repository: NickRepository

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PairingViewModel(service, repository) as T
    }
}