package com.github.group06.chelas_reversi.favourites

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.group06.chelas_reversi.domain.Game
import com.github.group06.chelas_reversi.domain.Player
import com.github.group06.chelas_reversi.screens.favourites.BACK_TAG
import com.github.group06.chelas_reversi.screens.favourites.FavoritesView
import com.github.group06.chelas_reversi.screens.favourites.GAME_CARD_FAVOURITE_TAG
import com.github.group06.chelas_reversi.screens.favourites.GAME_CARD_PLAY_TAG
import com.github.group06.chelas_reversi.screens.favourites.REPLAY_BACK_TAG
import com.github.group06.chelas_reversi.screens.favourites.REPLAY_FORWARD_TAG
import com.github.group06.chelas_reversi.screens.favourites.ReplayView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class FavouritesViewTest {

    @get:Rule
    val composeRuleTest = createComposeRule()

    @Test
    fun verify_if_all_stats_of_favourite_games_is_shown() {

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")

        val date1 = LocalDateTime.now()
        val date2 = LocalDateTime.now()

        composeRuleTest.setContent {
            FavoritesView(
                backIntent = {},
                games = listOf(
                    Game(me = Player("Pedro"), opponent = Player("João"), date = date1),
                    Game(me = Player("Diogo"), opponent = Player("Mihail"), date = date2)
                ),
                removeFromFavoritesIntent = {},
                startReplay = {}
            )
        }

        composeRuleTest.onNodeWithTag("game_Pedro-João").assertExists()
        composeRuleTest.onNodeWithTag("game_Diogo-Mihail").assertExists()
        composeRuleTest.onNodeWithTag("game_${formatter.format(date1)}")
        composeRuleTest.onNodeWithTag("game_${formatter.format(date2)}")
    }

    @Test
    fun verify_if_start_replay_intent_is_collected() {

        var callback = false;

        composeRuleTest.setContent {
            FavoritesView(
                backIntent = {},
                games = listOf(
                    Game(me = Player("Pedro"), opponent = Player("João"))
                ),
                removeFromFavoritesIntent = {},
                startReplay = {callback = true}
            )
        }

        composeRuleTest.onNodeWithTag(GAME_CARD_PLAY_TAG).assertExists().assertIsEnabled().performClick()

        assert(callback)

    }

    @Test
    fun verify_if_remove_from_favorites_intent_is_collected() {

        var callback = false;

        composeRuleTest.setContent {
            FavoritesView(
                backIntent = {},
                games = listOf(
                    Game(me = Player("Pedro"), opponent = Player("João"))
                ),
                removeFromFavoritesIntent = {callback = true},
                startReplay = {}
            )
        }

        composeRuleTest.onNodeWithTag(GAME_CARD_FAVOURITE_TAG).assertExists().assertIsEnabled().performClick()

        assert(callback)

    }

    @Test
    fun verify_if_back_intent_is_collected() {

        var callback = false;

        composeRuleTest.setContent {
            FavoritesView(
                backIntent = {callback = true},
                games = listOf(
                    Game(me = Player("Pedro"), opponent = Player("João"))
                ),
                removeFromFavoritesIntent = {},
                startReplay = {}
            )
        }

        composeRuleTest.onNodeWithTag(BACK_TAG).assertExists().assertIsEnabled().performClick()

        assert(callback)

    }

    @Test
    fun verify_if_back_from_replay_view_intent_is_collected() {

        var callback = false

        val game = Game(me = Player("Mihail"), opponent = Player("Vais perder"))

        composeRuleTest.setContent {
            ReplayView(
                game = game,
                play = game.plays.last(),
                previousPlay = {},
                nextPlay = {},
                backIntent = {callback = true}
            )
        }

        composeRuleTest.onNodeWithTag(BACK_TAG).assertExists().assertIsEnabled().performClick()

        assert(callback)

    }

    @Test
    fun verify_if_back_replay_arrow_intent_is_collected() {

        var callback = false

        val game = Game(me = Player("Mihail"), opponent = Player("Vais perder"))

        composeRuleTest.setContent {
            ReplayView(
                game = game,
                play = game.plays.last(),
                previousPlay = {callback = true},
                nextPlay = {},
                backIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(REPLAY_BACK_TAG).assertExists().assertIsEnabled().performClick()

        assert(callback)

    }

    @Test
    fun verify_if_forward_replay_arrow_intent_is_collected() {

        var callback = false

        val game = Game(me = Player("Mihail"), opponent = Player("Vais perder"))

        composeRuleTest.setContent {
            ReplayView(
                game = game,
                play = game.plays.last(),
                previousPlay = {},
                nextPlay = {callback = true},
                backIntent = {}
            )
        }

        composeRuleTest.onNodeWithTag(REPLAY_FORWARD_TAG).assertExists().assertIsEnabled().performClick()

        assert(callback)

    }


}