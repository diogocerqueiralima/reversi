package com.github.group06.chelas_reversi.nick

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.group06.chelas_reversi.screens.nick.CANCEL_BUTTON_TAG
import com.github.group06.chelas_reversi.screens.nick.NickDisplayingView
import com.github.group06.chelas_reversi.screens.nick.OK_BUTTON_TAG
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NickDisplayingViewTest {

    @get:Rule
    val composeTree = createComposeRule()

    @Test
    fun verify_if_nick_is_displayed_on_displaying_view() {

        composeTree.setContent {
            NickDisplayingView(
                nickname = "Pedro",
                onNicknameChange = {},
                isEditing = false,
                onOkClick = {},
                onCancelClick = {}
            )
        }

        composeTree.onNodeWithTag("nickname_Pedro")
            .assertIsDisplayed()
            .assertTextEquals("Pedro")
    }

    @Test
    fun check_if_when_insert_a_character_on_text_field_of_displaying_view_the_intent_is_collected() {

        var callback = false

        composeTree.setContent {
            NickDisplayingView(
                nickname = "Pedro",
                onNicknameChange = { callback = true },
                isEditing = false,
                onOkClick = {},
                onCancelClick = {}
            )
        }

        composeTree.onNodeWithTag("nickname_Pedro").performTextInput("a")
        assertTrue(callback)
    }

    @Test
    fun check_when_buttons_are_in_correct_state_in_displaying_view_but_is_not_editing() {

        composeTree.setContent {
            NickDisplayingView(
                nickname = "Pedro",
                onNicknameChange = {},
                isEditing = false,
                onOkClick = {},
                onCancelClick = {}
            )
        }

        composeTree.onNodeWithTag(OK_BUTTON_TAG)
            .assertIsDisplayed()
            .assertIsNotEnabled()

        composeTree.onNodeWithTag(CANCEL_BUTTON_TAG)
            .assertIsDisplayed()
            .assertIsEnabled()

    }

    @Test
    fun check_when_buttons_are_in_correct_state_in_displaying_view_but_is_editing() {

        composeTree.setContent {
            NickDisplayingView(
                nickname = "Pedro",
                onNicknameChange = {},
                isEditing = true,
                onOkClick = {},
                onCancelClick = {}
            )
        }

        composeTree.onNodeWithTag(OK_BUTTON_TAG)
            .assertIsDisplayed()
            .assertIsEnabled()

        composeTree.onNodeWithTag(CANCEL_BUTTON_TAG)
            .assertIsDisplayed()
            .assertIsEnabled()

    }

    @Test
    fun check_if_ok_intent_is_collected() {

        var callback = false

        composeTree.setContent {
            NickDisplayingView(
                nickname = "Pedro",
                onNicknameChange = {},
                isEditing = true,
                onOkClick = { callback = true },
                onCancelClick = {}
            )
        }

        composeTree.onNodeWithTag(OK_BUTTON_TAG)
            .assertIsDisplayed()
            .performClick()

        assertTrue(callback)
    }

    @Test
    fun check_if_cancel_intent_is_collected() {

        var callback = false

        composeTree.setContent {
            NickDisplayingView(
                nickname = "Pedro",
                onNicknameChange = {},
                isEditing = true,
                onCancelClick = { callback = true },
                onOkClick = {}
            )
        }

        composeTree.onNodeWithTag(CANCEL_BUTTON_TAG)
            .assertIsDisplayed()
            .performClick()

        assertTrue(callback)
    }

}