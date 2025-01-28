package com.github.group06.chelas_reversi.favourites

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Play
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.screens.favourites.DELETING_VIEW_TAG
import com.github.group06.chelas_reversi.screens.favourites.FavoriteState
import com.github.group06.chelas_reversi.screens.favourites.FavoritesScreen
import com.github.group06.chelas_reversi.screens.favourites.FavoritesViewModel
import com.github.group06.chelas_reversi.screens.favourites.LISTENING_VIEW_TAG
import com.github.group06.chelas_reversi.screens.favourites.LOADING_VIEW_TAG
import com.github.group06.chelas_reversi.screens.favourites.REPLAY_VIEW_TAG
import com.github.group06.chelas_reversi.services.FakeFavoritesService
import org.junit.Rule
import org.junit.Test

class FavouritesScreenTest {

    @get:Rule
    val composeRuleTest = createComposeRule()

    @Test
    fun verify_if_favourites_screen_is_showing() {

        composeRuleTest.setContent {
            FavoritesScreen(
                viewModel = FavoritesViewModel(
                    service = FakeFavoritesService()
                ),
                navigateToPairing = {}
            )
        }

        composeRuleTest.onNodeWithTag(LOADING_VIEW_TAG).assertExists()

    }

    @Test
    fun verify_if_favourites_listing_screen_is_showing() {

        composeRuleTest.setContent {
            FavoritesScreen(
                viewModel = FavoritesViewModel(
                    service = FakeFavoritesService(),
                    initialState = FavoriteState.Listing(emptyList())
                ),
                navigateToPairing = {}
            )
        }

        composeRuleTest.onNodeWithTag(LISTENING_VIEW_TAG).assertExists()

    }

    @Test
    fun verify_if_favourites_deleting_screen_is_showing() {

        composeRuleTest.setContent {
            FavoritesScreen(
                viewModel = FavoritesViewModel(
                    service = FakeFavoritesService(),
                    initialState = FavoriteState.Deleting
                ),
                navigateToPairing = {}
            )
        }

        composeRuleTest.onNodeWithTag(DELETING_VIEW_TAG).assertExists()

    }

    @Test
    fun verify_if_favourites_replay_screen_is_showing() {

        composeRuleTest.setContent {
            FavoritesScreen(
                viewModel = FavoritesViewModel(
                    service = FakeFavoritesService(),
                    initialState = FavoriteState.Replay(Game(me = Player("Mihail"), opponent = Player("Diogo")), play = Play(player = null, slots = emptyList()), 0)
                ),
                navigateToPairing = {}
            )
        }

        composeRuleTest.onNodeWithTag(REPLAY_VIEW_TAG).assertExists()

    }
}