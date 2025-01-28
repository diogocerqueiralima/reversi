package com.github.group06.chelas_reversi.pairing

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.group06.chelas_reversi.infrastructure.NickRepositoryImpl
import com.github.group06.chelas_reversi.screens.pairing.PairingState
import com.github.group06.chelas_reversi.screens.pairing.PairingViewModel
import com.github.group06.chelas_reversi.services.FakePairingService
import com.github.group06.chelas_reversi.utils.CleanDataStoreRule
import com.github.group06.chelas_reversi.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PairingViewModelTest {

    @get:Rule
    val dispatcherRule = ReplaceMainDispatcherRule()

    @get:Rule
    val cleanDataStoreRule = CleanDataStoreRule()

    @Test
    fun initial_state_should_be_idle() {

        val viewModel = PairingViewModel(
            service = FakePairingService(),
            nickRepository = NickRepositoryImpl(cleanDataStoreRule.dataStore)
        )

        assert(viewModel.state.value is PairingState.IdlePairingState) {
            "Expected state is Idle, but was ${viewModel.state}"
        }

    }

    @Test
    fun search_game_with_invalid_nick_should_transit_to_error_state() = runTest {

        val viewModel = PairingViewModel(
            service = FakePairingService(),
            nickRepository = NickRepositoryImpl(cleanDataStoreRule.dataStore)
        )

        viewModel.startSearching {  }

        val errorState = viewModel.state.first { it is PairingState.ErrorPairingState }

        assert(errorState is PairingState.ErrorPairingState) {
            "Expected ErrorPairingState, but was $errorState"
        }

    }

    @Test
    fun search_game_with_invalid_nick_should_transit_to_idle_state_after_error_state() = runTest {

        val viewModel = PairingViewModel(
            service = FakePairingService(),
            nickRepository = NickRepositoryImpl(cleanDataStoreRule.dataStore)
        )

        viewModel.startSearching {  }
        viewModel.searchPlayersJob?.join()

        assert(viewModel.state.value is PairingState.IdlePairingState) {
            "Expected ErrorPairingState, but was ${viewModel.state.value}"
        }
    }

    @Test
    fun idle_transition_to_searching_state() = runTest(dispatcherRule.testDispatcher) {

        val viewModel = PairingViewModel(
            service = FakePairingService(),
            nickRepository = NickRepositoryImpl(cleanDataStoreRule.dataStore)
        )

        viewModel.startSearching {}

        assert(
            viewModel.state.value is PairingState.IdlePairingState
        ) { "Expected state is Searching, but was ${viewModel.state.value}" }
    }

}