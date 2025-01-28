package com.github.group06.chelas_reversi.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Piece
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.domain.Slot
import com.github.group06.chelas_reversi.domain.Timer
import com.github.group06.chelas_reversi.exceptions.StopListeningGameChangeColorException
import com.github.group06.chelas_reversi.exceptions.TimerCancelException
import com.github.group06.chelas_reversi.infrastructure.NickRepository
import com.github.group06.chelas_reversi.services.GameService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

sealed interface GameState {

    data object Loading : GameState
    data object Saving : GameState
    data class ChooseColorState(val game: Game, val timer: Timer) : GameState
    data class PlayingState(val game: Game) : GameState
    data class AlreadyPlayedState(val game: Game) : GameState
    data class WaitingState(val game: Game) : GameState
    data class FinishedState(val game: Game) : GameState
    data class ErrorGameState(val error: GameError) : GameState

}

enum class GameError {

    AWAIT_COLOR,
    INVALID_NICK

}

class GameViewModel(

    private val gameService: GameService,
    private val nickRepository: NickRepository,
    initialState: GameState = GameState.Loading

) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<GameState> = _state.asStateFlow()
    private val timerColor = MutableStateFlow(Timer(10))

    private val awaitColorChooseCancelDelay = 1000L * 5
    private var listeningChangesOfColorJob : Job? = null
    private var timerColorJob : Job? = null

    private val timeout = 1000L*30

    fun addGameToFavorite(game: Game, navigateToIdleIntent: () -> Unit) {

        if (_state.value !is GameState.FinishedState)
            return

        _state.value = GameState.Saving

        viewModelScope.launch {

            gameService.addGameToFavorite(game)
            navigateToIdleIntent()

        }

    }

    fun chooseSlot(game: Game, slot: Slot){

        if (_state.value !is GameState.PlayingState || slot.piece != Piece.NONE)
            return

        _state.value = GameState.AlreadyPlayedState(game)

        viewModelScope.launch {

            val newGame = gameService.chooseSlot(game, slot) ?: run {
                _state.value = GameState.PlayingState(game)
                return@launch
            }

            if (newGame.gameOver) {
                _state.value = GameState.FinishedState(newGame)
                return@launch
            }

            _state.value = GameState.WaitingState(newGame)
            awaitPlay(newGame, newGame.opponent)

        }

    }

    private suspend fun awaitTimeout(game: Game) {

        gameService.awaitPlay(game).collectLatest {

            if (it.gameOver) {
                _state.value = GameState.FinishedState(it)
                gameService.deleteGame(it)
                return@collectLatest
            }

        }

    }

    private suspend fun awaitPlay(game: Game, player: Player = game.me) {

        try {
            withTimeout(timeout) {

                gameService.awaitPlay(game).collectLatest {

                    if (it.gameOver) {
                        _state.value = GameState.FinishedState(it)
                        gameService.deleteGame(it)
                        return@collectLatest
                    }

                    if (!gameService.hasAnyPlay(it)) {
                        val theGame = gameService.pass(it)
                        _state.value = GameState.WaitingState(theGame)
                        awaitPlay(theGame)
                        return@collectLatest
                    }

                    _state.value = GameState.PlayingState(it)
                    awaitTimeout(it)
                }
            }
        }catch(e: Exception){
            val newGame = gameService.ff(game, player)
            _state.value = GameState.FinishedState(newGame)
        }

    }

    fun loadGame(gameId: String, navigateToIdleIntent: () -> Unit) {

        if (_state.value !is GameState.Loading)
            return

        viewModelScope.launch {

            val nick = nickRepository.getNick()

            if (nick == null) {
                _state.value = GameState.ErrorGameState(error = GameError.INVALID_NICK)
                return@launch
            }

            val game = gameService.loadGame(gameId, Player(nick))
            _state.value = GameState.ChooseColorState(game, timerColor.value)

            startListenChangesOfColor(game, navigateToIdleIntent)
        }

    }

    fun chooseColor(piece: Piece, game: Game) {

        if (_state.value !is GameState.ChooseColorState)
            return

        viewModelScope.launch {

            val newGame = game.copy(me = game.me.copy(piece = piece))
            _state.value = GameState.ChooseColorState(newGame, timerColor.value)
            gameService.chooseColor(piece, newGame)

        }

    }

    private fun startListenChangesOfColor(game: Game, navigateToIdleIntent: () -> Unit) {

        listeningChangesOfColorJob = viewModelScope.launch {

            try {

                gameService.listeningChangesOfColors(game).collectLatest {
                    _state.value = GameState.ChooseColorState(it, timerColor.value)
                }

            }catch (_: StopListeningGameChangeColorException) {}

        }

        timerColorJob = viewModelScope.launch {

            try {

                while (timerColor.value.value > 0) {
                    delay(1000)
                    timerColor.value = --timerColor.value
                    val currentGame = (_state.value as? GameState.ChooseColorState)?.game ?: game
                    _state.value = GameState.ChooseColorState(currentGame, timerColor.value)
                }

            }catch(_: TimerCancelException){}
            catch(e: Exception){
                listeningChangesOfColorJob?.cancel(StopListeningGameChangeColorException())
                _state.value = GameState.ErrorGameState(error = GameError.AWAIT_COLOR)
                delay(awaitColorChooseCancelDelay)
                gameService.deleteGame(game)
                navigateToIdleIntent()
            }
        }

        viewModelScope.launch {

            gameService.awaitColorChoice(game).collectLatest { newGame ->

                listeningChangesOfColorJob?.cancel(StopListeningGameChangeColorException())
                timerColorJob?.cancel(TimerCancelException())

                if (newGame.me.piece == Piece.BLACK) {
                    _state.value = GameState.PlayingState(newGame)
                    awaitTimeout(newGame)
                }else {
                    _state.value = GameState.WaitingState(newGame)
                    awaitPlay(newGame)
                }

            }

        }

    }

}

@Suppress("UNCHECKED_CAST")
class GameViewModelFactory(

    private val service: GameService,
    private val repository: NickRepository

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(service, repository) as T
    }
}