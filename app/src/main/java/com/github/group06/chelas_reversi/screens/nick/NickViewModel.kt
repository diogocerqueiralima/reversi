package com.github.group06.chelas_reversi.screens.nick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.group06.chelas_reversi.domain.Nick
import com.github.group06.chelas_reversi.domain.isNickValid
import com.github.group06.chelas_reversi.infrastructure.NickRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface NickState {

    data object Loading : NickState
    data object Error : NickState
    data object Saving : NickState
    data class Displaying(val nickname: String, val isEditing: Boolean) : NickState

}

class NickViewModel(

    private val nickRepository: NickRepository,
    initialState: NickState = NickState.Loading

) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<NickState> = _state.asStateFlow()

    private val delay = 1000L

    fun load(): Job? {

        if (_state.value !is NickState.Loading)
            return null

        return viewModelScope.launch {

            val nick = nickRepository.getNick()
            delay(delay)
            _state.value = NickState.Displaying(nick?.value ?: "", false)

        }

    }

    fun onNicknameChange(nickname: String) {

        if (_state.value !is NickState.Displaying)
            return

        _state.value = NickState.Displaying(nickname, true)
    }

    fun onOk(nickname: String, navigateToPairing: () -> Unit): Job? {

        if (_state.value !is NickState.Displaying)
            return null

        return viewModelScope.launch {

            if (!isNickValid(nickname)) {
                _state.value = NickState.Error
            }else {
                _state.value = NickState.Saving
                nickRepository.updateNick(Nick(value = nickname))
            }

            delay(delay)
            navigateToPairing()
        }

    }

    fun onCancel(navigateToPairing: () -> Unit) {

        if (_state.value !is NickState.Displaying)
            return

        navigateToPairing()
    }

}

@Suppress("UNCHECKED_CAST")
class NickViewModelFactory(

    private val repository: NickRepository

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NickViewModel(repository) as T
    }
}