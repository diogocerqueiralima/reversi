package com.github.group06.chelas_reversi.author

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.group06.chelas_reversi.screens.author.AUTHOR_SCREEN_TAG
import com.github.group06.chelas_reversi.screens.author.AuthorActivity
import com.github.group06.chelas_reversi.screens.author.BACK_TAG
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthorActivityTest {

    @get:Rule
    val testRule = createAndroidComposeRule<AuthorActivity>()

    @Test
    fun initially_the_screen_is_displayed() {
        testRule.onNodeWithTag(AUTHOR_SCREEN_TAG).assertExists()
    }

    @Test
    fun when_navigate_to_back_the_activity_is_finished() {

        testRule.waitUntil { testRule.activityRule.scenario.state.isAtLeast(Lifecycle.State.STARTED) }
        testRule.onNodeWithTag(BACK_TAG).performClick()
        testRule.waitUntil { testRule.activityRule.scenario.state.isAtLeast(Lifecycle.State.DESTROYED) }

    }

}