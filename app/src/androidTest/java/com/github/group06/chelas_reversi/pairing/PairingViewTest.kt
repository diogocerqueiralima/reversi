package com.github.group06.chelas_reversi.pairing

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.screens.author.AUTHOR_SCREEN_TAG
import com.github.group06.chelas_reversi.screens.pairing.FOUND_ANIMATION_TAG
import com.github.group06.chelas_reversi.screens.pairing.FOUND_PLAYER_TAG
import com.github.group06.chelas_reversi.screens.pairing.FOUND_TEXT_TAG
import com.github.group06.chelas_reversi.screens.pairing.IDLE_FAVOURITES_BUTTON_TAG
import com.github.group06.chelas_reversi.screens.pairing.IDLE_IMAGE_TAG
import com.github.group06.chelas_reversi.screens.pairing.IDLE_NAVIGATE_AUTHOR_BUTTON_TAG
import com.github.group06.chelas_reversi.screens.pairing.IDLE_PLAY_BUTTON_TAG
import com.github.group06.chelas_reversi.screens.pairing.IDLE_QUIT_BUTTON_TAG
import com.github.group06.chelas_reversi.screens.pairing.IDLE_IMAGE_TAG
import com.github.group06.chelas_reversi.screens.pairing.IDLE_NAVIGATE_AUTHOR_BUTTON_TAG
import com.github.group06.chelas_reversi.screens.pairing.PLAYER_CARD_TEXT_TAG
import com.github.group06.chelas_reversi.screens.pairing.PairingCancelSearchingView
import com.github.group06.chelas_reversi.screens.pairing.PairingFoundView
import com.github.group06.chelas_reversi.screens.pairing.PairingIdleView
import com.github.group06.chelas_reversi.screens.pairing.PairingScreen
import com.github.group06.chelas_reversi.screens.pairing.PairingSearchingView
import com.github.group06.chelas_reversi.screens.pairing.PairingViewModel
import com.github.group06.chelas_reversi.screens.pairing.PlayerCard
import com.github.group06.chelas_reversi.screens.pairing.SEARCHING_ANIMATION_TAG
import com.github.group06.chelas_reversi.screens.pairing.SEARCHING_BUTTON_TAG
import com.github.group06.chelas_reversi.screens.pairing.SEARCHING_PLAYER_LIST_TAG
import com.github.group06.chelas_reversi.screens.pairing.SEARCHING_TEXT_TAG
import com.github.group06.chelas_reversi.services.FakePairingService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PairingViewTest {

    @get:Rule
    val composeRuleTest = createComposeRule()

    @Test
    fun verify_if_all_components_of_idle_pairing_view_exists() {

        composeRuleTest.setContent {
            PairingIdleView(onStartSearchingClick = {}, authorScreenIntent = {}, quitScreenIntent = {}, nickScreenIntent = {}, favoritesScreenIntent = {})
        }

        composeRuleTest.onNodeWithTag(IDLE_NAVIGATE_AUTHOR_BUTTON_TAG).assertExists()
        composeRuleTest.onNodeWithTag(IDLE_IMAGE_TAG).assertExists()
        composeRuleTest.onNodeWithTag(IDLE_PLAY_BUTTON_TAG).assertExists()
        composeRuleTest.onNodeWithTag(IDLE_FAVOURITES_BUTTON_TAG).assertExists()
        composeRuleTest.onNodeWithTag(IDLE_QUIT_BUTTON_TAG).assertExists()
    }

    @Test
    fun verify_if_all_components_of_searching_pairing_view_exists() {

        composeRuleTest.setContent {
            PairingSearchingView(onCancelClick = {})
        }

        composeRuleTest.onNodeWithTag(SEARCHING_ANIMATION_TAG).assertExists()
        composeRuleTest.onNodeWithTag(SEARCHING_TEXT_TAG).assertExists()
        composeRuleTest.onNodeWithTag(SEARCHING_BUTTON_TAG).assertExists()
        composeRuleTest.onNodeWithTag(SEARCHING_PLAYER_LIST_TAG).assertExists()

    }

    @Test
    fun verify_if_searching_pairing_view_when_listing_players_is_showing_all_of_them() {

        composeRuleTest.setContent {
            PairingSearchingView(onCancelClick = {}, players = listOf(Player("João"), Player("Pedro")))
        }

        composeRuleTest.onNodeWithTag("player_João").assertExists()
        composeRuleTest.onNodeWithTag("player_Pedro").assertExists()
    }

    @Test
    fun verify_if_player_card_show_all_components() {

        composeRuleTest.setContent {
            PlayerCard(player = Player("João"))
        }

        composeRuleTest.onNodeWithTag(PLAYER_CARD_TEXT_TAG).assertExists()
    }

    @Test
    fun verify_if_all_components_of_found_pairing_view_exists() {

        composeRuleTest.setContent {
            PairingFoundView()
        }

        composeRuleTest.onNodeWithTag(FOUND_PLAYER_TAG).assertExists()
        composeRuleTest.onNodeWithTag(FOUND_TEXT_TAG).assertExists()
        composeRuleTest.onNodeWithTag(FOUND_ANIMATION_TAG).assertExists()

    }

    @Test
    fun verify_if_quit_intent_is_collected() {

        var callback = false

        composeRuleTest.setContent {
            PairingIdleView(onStartSearchingClick = {}, quitScreenIntent = { callback = true }, authorScreenIntent = {}, nickScreenIntent = {}, favoritesScreenIntent = {})
        }

        composeRuleTest.onNodeWithTag(IDLE_QUIT_BUTTON_TAG).performClick()
        assert(callback)

    }

    @Test
    fun verify_if_navigate_to_author_screen_intent_is_collected() {

        var callback = false

        composeRuleTest.setContent {
            PairingIdleView(onStartSearchingClick = {}, quitScreenIntent = {}, authorScreenIntent = { callback = true }, nickScreenIntent = {}, favoritesScreenIntent = {})
        }

        composeRuleTest.onNodeWithTag(IDLE_NAVIGATE_AUTHOR_BUTTON_TAG).performClick()
        assert(callback)

    }

    @Test
    fun verify_if_play_intent_is_collected() {

        var callback = false

        composeRuleTest.setContent {
            PairingIdleView(onStartSearchingClick = { callback = true }, quitScreenIntent = {}, authorScreenIntent = {}, nickScreenIntent = {}, favoritesScreenIntent = {})
        }

        composeRuleTest.onNodeWithTag(IDLE_PLAY_BUTTON_TAG).performClick()
        assert(callback)
    }

    @Test
    fun verify_if_cancel_searching_game_intent_is_collected() {

        var callback = false

        composeRuleTest.setContent {
            PairingSearchingView(onCancelClick = { callback = true })
        }

        composeRuleTest.onNodeWithTag(SEARCHING_BUTTON_TAG).performClick()
        assert(callback)

    }

}