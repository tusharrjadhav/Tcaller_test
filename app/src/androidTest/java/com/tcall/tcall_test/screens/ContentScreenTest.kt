package com.tcall.tcall_test.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tcall.tcall_test.R
import com.tcall.tcall_test.ui.theme.TCall_testTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
class ContentScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var mockViewModel: MainScreenViewModel

    // MutableStates to control the ViewModel's state from the test
    private lateinit var tenthCharState: MutableState<ScreenState>
    private lateinit var every10thCharState: MutableState<ScreenState>
    private lateinit var charCountState: MutableState<ScreenState>
    
    private val targetContext by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) // Initialize mocks

        tenthCharState = mutableStateOf(ScreenState())
        every10thCharState = mutableStateOf(ScreenState())
        charCountState = mutableStateOf(ScreenState())

        whenever(mockViewModel.tenthChar).thenReturn(tenthCharState)
        whenever(mockViewModel.every10thChar).thenReturn(every10thCharState)
        whenever(mockViewModel.charCount).thenReturn(charCountState)
    }

    private fun setContent() {
        composeTestRule.setContent {
            TCall_testTheme {
                ContentScreen(viewModel = mockViewModel)
            }
        }
    }

    @Test
    fun displayInitialState_showsButton() {
        setContent()
        composeTestRule.onNodeWithText(targetContext.getString(R.string.fetch_data)).assertIsDisplayed()
        // Initial state texts might be empty or show "Loading..." if default is loading
        composeTestRule.onNodeWithText("10th Character: ").assertIsDisplayed() // Empty data
        composeTestRule.onNodeWithText("Word Count: ").assertIsDisplayed() // Empty data
        composeTestRule.onNodeWithText("Every 10th Character: ").assertIsDisplayed() // Empty data
    }

    @Test
    fun displayLoadingState_showsLoadingText() {
        tenthCharState.value = ScreenState(loading = true)
        every10thCharState.value = ScreenState(loading = true)
        charCountState.value = ScreenState(loading = true)
        
        setContent()

        composeTestRule.onNodeWithText(targetContext.getString(R.string.fetch_data)).assertIsDisplayed()
        composeTestRule.onNodeWithText("10th Character: Loading...").assertIsDisplayed()
        composeTestRule.onNodeWithText("Word Count: Loading...").assertIsDisplayed()
        composeTestRule.onNodeWithText("Every 10th Character: Loading...").assertIsDisplayed()
    }

    @Test
    fun displayContent_SuccessPath_showsData() {
        tenthCharState.value = ScreenState(data = "A")
        every10thCharState.value = ScreenState(data = "B,C,D")
        charCountState.value = ScreenState(data = "Count: 3")

        setContent()

        composeTestRule.onNodeWithText(targetContext.getString(R.string.fetch_data)).assertIsDisplayed()
        composeTestRule.onNodeWithText("10th Character: A").assertIsDisplayed()
        composeTestRule.onNodeWithText("Word Count: Count: 3").assertIsDisplayed()
        composeTestRule.onNodeWithText("Every 10th Character: B,C,D").assertIsDisplayed()
    }

    @Test
    fun displayContent_ErrorPath_showsErrorMessages() {
        tenthCharState.value = ScreenState(error = "Error 1")
        every10thCharState.value = ScreenState(error = "Error 2")
        charCountState.value = ScreenState(error = "Error 3")

        setContent()

        composeTestRule.onNodeWithText(targetContext.getString(R.string.fetch_data)).assertIsDisplayed()
        composeTestRule.onNodeWithText("10th Character: Error 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Word Count: Error 3").assertIsDisplayed()
        composeTestRule.onNodeWithText("Every 10th Character: Error 2").assertIsDisplayed()
    }

    @Test
    fun fetchDataButton_onClick_callsViewModelFetchContent() {
        setContent()
        composeTestRule.onNodeWithText(targetContext.getString(R.string.fetch_data)).performClick()
        verify(mockViewModel).fetchContent()
    }
}
