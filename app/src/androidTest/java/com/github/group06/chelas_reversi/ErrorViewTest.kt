package com.github.group06.chelas_reversi

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.group06.chelas_reversi.screens.ANIMATION_TAG
import com.github.group06.chelas_reversi.screens.DEFAULT_MESSAGE_TAG
import com.github.group06.chelas_reversi.screens.ErrorView
import com.github.group06.chelas_reversi.screens.MESSAGE_TAG
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ErrorViewTest {

    @get:Rule
    val composeRuleTest = createComposeRule()

    @Test
    fun check_if_all_components_exists() {

        composeRuleTest.setContent {
            ErrorView(msg = "error")
        }

        composeRuleTest.onNodeWithTag(DEFAULT_MESSAGE_TAG).assertExists()
        composeRuleTest.onNodeWithTag(MESSAGE_TAG).assertExists()
        composeRuleTest.onNodeWithTag(ANIMATION_TAG).assertExists()
    }

}