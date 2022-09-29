package com.tcall.tcall_test.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tcall.tcall_test.R
import com.tcall.tcall_test.databinding.FragmentContentBinding
import com.tcall.tcall_test.repository.DataRepository
import com.tcall.tcall_test.util.DataResult
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
internal class ContentFragmentTest {

    private val TEST_CHAR_1 = "Test 10th char"
    private val TEST_CHAR_2 = "Test every 10th char"
    private val TEST_CHAR_3 = "Test wordCount"

    /*@get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)*/

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainScreenViewModel
    private lateinit var mockBinding: FragmentContentBinding
    private lateinit var mockDataModel: DataModel

    @Before
    fun init() {
        //hiltRule.inject()
        viewModel = mock(MainScreenViewModel::class.java)
        mockDataModel = mock(DataModel::class.java)
        mockBinding = mock(FragmentContentBinding::class.java)

        `when`(viewModel.dataModel).thenReturn(mockDataModel)

        `when`(mockDataModel.char10th).thenReturn(TEST_CHAR_1)
        `when`(mockDataModel.every10thChar).thenReturn(TEST_CHAR_2)
        `when`(mockDataModel.wordCount).thenReturn(TEST_CHAR_3)

    }

    @Test
    fun testUIInteraction() {
        // Check Button is display
        onView(withId(R.id.btnSubmit)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSubmit)).perform(click())

        onView(withId(R.id.tv1Content)).check(
            matches(
                withText(TEST_CHAR_1)
            )
        )
        onView(withId(R.id.tv2Content)).check(
            matches(
                withText(TEST_CHAR_2)
            )
        )
        onView(withId(R.id.tv3Content)).check(
            matches(
                withText(TEST_CHAR_3)
            )
        )
    }

}