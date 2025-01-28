package com.github.group06.chelas_reversi.nick

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.group06.chelas_reversi.domain.Nick
import com.github.group06.chelas_reversi.infrastructure.NickRepository
import com.github.group06.chelas_reversi.screens.nick.NickState
import com.github.group06.chelas_reversi.screens.nick.NickViewModel
import com.github.group06.chelas_reversi.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NickViewModelTest {

    @get:Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    private val fakeRepo = object : NickRepository {
        override suspend fun getNick(): Nick {
            delay(1000L)
            return Nick(value = "John")
        }

        override suspend fun updateNick(nick: Nick) {}
    }

    @Test
    fun initial_state_is_loading() {

        val model = NickViewModel(fakeRepo)
        val state = model.state.value
        assert(state is NickState.Loading)

    }

    @Test
    fun load_transits_to_displaying_and_not_editing() = runTest {

        val model = NickViewModel(fakeRepo)
        model.load()?.join()
        val state = model.state.value
        assert(state is NickState.Displaying && !state.isEditing)

    }

    @Test
    fun call_load_has_no_effect_if_state_is_not_loading() = runTest {

        val model = NickViewModel(fakeRepo, initialState = NickState.Displaying(nickname = "John", false))
        model.load()?.join()
        val state = model.state.value
        assert(state is NickState.Displaying && !state.isEditing)
    }

    @Test
    fun when_change_nickname_in_displaying_view_set_editing_to_true() {

        val model = NickViewModel(fakeRepo, initialState = NickState.Displaying(nickname = "", isEditing = false))
        model.onNicknameChange("P")
        val state = model.state.value
        assert(state is NickState.Displaying && state.isEditing)

    }

    @Test
    fun call_on_nickname_change_has_no_effect_if_state_is_not_displaying() {

        val model = NickViewModel(fakeRepo, initialState = NickState.Saving)
        model.onNicknameChange("P")
        val state = model.state.value
        assert(state is NickState.Saving)

    }

    @Test
    fun on_ok_change_state_to_saving_if_nickname_is_valid() = runTest {

        val model = NickViewModel(fakeRepo, initialState = NickState.Displaying("John", true))
        model.onOk("John") {}?.join()
        val state = model.state.value
        assert(state is NickState.Saving)

    }

    @Test
    fun call_on_ok_with_invalid_nickname_change_to_error_state() = runTest {

        val model = NickViewModel(fakeRepo, initialState = NickState.Displaying("", true))
        model.onOk("J") {}?.join()
        val state = model.state.value
        assert(state is NickState.Error)

    }

    @Test
    fun call_on_ok_has_no_effect_if_state_is_not_displaying() {

        val model = NickViewModel(fakeRepo, initialState = NickState.Saving)
        model.onOk("John") {}
        val state = model.state.value
        assert(state is NickState.Saving)

    }

    @Test
    fun call_on_cancel_change_to_navigate() {

        var callback = false
        val model = NickViewModel(fakeRepo, initialState = NickState.Displaying("", false))
        model.onCancel { callback = true }
        assert(callback)

    }

    @Test
    fun call_on_cancel_has_no_effect_if_state_is_not_displaying() {

        var callback = false
        val model = NickViewModel(fakeRepo, initialState = NickState.Saving)
        model.onCancel { callback = true }
        assert(!callback)

    }

}