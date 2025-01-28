package com.github.group06.chelas_reversi.author

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.group06.chelas_reversi.screens.author.AuthorScreen
import com.github.group06.chelas_reversi.screens.author.BACK_TAG
import com.github.group06.chelas_reversi.screens.author.EMAIL_BUTTON_TAG
import com.github.group06.chelas_reversi.screens.author.IMAGE_TAG

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class AuthorScreenTest {

    @get:Rule
    val composeRuleTest = createComposeRule()

    @Test
    fun verify_if_author_screen_shows_all_components() {

        composeRuleTest.setContent {
            AuthorScreen(backIntent = {}, emailIntent = {})
        }

        composeRuleTest.onNodeWithTag(EMAIL_BUTTON_TAG).assertIsEnabled()
        composeRuleTest.onNodeWithTag(IMAGE_TAG).assertExists()
        composeRuleTest.onNodeWithTag(BACK_TAG).assertExists()
        composeRuleTest.onNodeWithTag("author_50870").assertExists()
        composeRuleTest.onNodeWithTag("author_50879").assertExists()
    }

    @Test
    fun on_click_to_back_check_callback() {

        var callback = false

        composeRuleTest.setContent {
            AuthorScreen(backIntent = { callback = true }, emailIntent = {})
        }

        composeRuleTest.onNodeWithTag(BACK_TAG).performClick()
        assertTrue(callback)
    }

    @Test
    fun on_click_to_send_email_check_callback() {

        var callback = false

        composeRuleTest.setContent {
            AuthorScreen(backIntent = {}, emailIntent = { callback = true })
        }

        composeRuleTest.onNodeWithTag(EMAIL_BUTTON_TAG).performClick()
        assertTrue(callback)
    }

}