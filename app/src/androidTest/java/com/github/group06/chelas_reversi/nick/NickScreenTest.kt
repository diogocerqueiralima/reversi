package com.github.group06.chelas_reversi.nick

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.group06.chelas_reversi.DependenciesContainer
import com.github.group06.chelas_reversi.infrastructure.NickRepositoryImpl
import com.github.group06.chelas_reversi.screens.nick.NICK_DISPLAYING_VIEW_TAG
import com.github.group06.chelas_reversi.screens.nick.NICK_ERROR_VIEW_TAG
import com.github.group06.chelas_reversi.screens.nick.NICK_LOADING_VIEW_TAG
import com.github.group06.chelas_reversi.screens.nick.NICK_SAVING_VIEW_TAG
import com.github.group06.chelas_reversi.screens.nick.NickScreen
import com.github.group06.chelas_reversi.screens.nick.NickState
import com.github.group06.chelas_reversi.screens.nick.NickViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NickScreenTest {

    @get:Rule
    val composeRuleTest = createComposeRule()

    private val dataStore by lazy {
        val dependenciesContainer = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as DependenciesContainer
        dependenciesContainer.preferencesDataStore
    }

    @Test
    fun verify_if_loading_view_is_shown() {

        composeRuleTest.setContent {
            NickScreen(
                viewModel = NickViewModel(
                    nickRepository = NickRepositoryImpl(dataStore)
                ),
                navigateToPairing = {}
            )
        }

        composeRuleTest.onNodeWithTag(NICK_LOADING_VIEW_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun verify_if_saving_view_is_shown() {

        composeRuleTest.setContent {
            NickScreen(
                viewModel = NickViewModel(
                    nickRepository = NickRepositoryImpl(dataStore),
                    initialState = NickState.Saving
                ),
                navigateToPairing = {}
            )
        }

        composeRuleTest.onNodeWithTag(NICK_SAVING_VIEW_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun verify_if_displaying_view_is_shown() {

        composeRuleTest.setContent {
            NickScreen(
                viewModel = NickViewModel(
                    nickRepository = NickRepositoryImpl(dataStore),
                    initialState = NickState.Displaying("", isEditing = false)
                ),
                navigateToPairing = {}
            )
        }

        composeRuleTest.onNodeWithTag(NICK_DISPLAYING_VIEW_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun verify_if_error_view_is_shown() {

        composeRuleTest.setContent {
            NickScreen(
                viewModel = NickViewModel(
                    nickRepository = NickRepositoryImpl(dataStore),
                    initialState = NickState.Error
                ),
                navigateToPairing = {}
            )
        }

        composeRuleTest.onNodeWithTag(NICK_ERROR_VIEW_TAG)
            .assertIsDisplayed()
    }

}