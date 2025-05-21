package com.tcall.tcall_test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainActivity_displaysContentScreenSuccessfully() {
        // Verify the "Fetch Data" button from ContentScreen is displayed using its test tag.
        composeTestRule.onNodeWithTag("FetchDataButton").assertIsDisplayed()
    }
}
