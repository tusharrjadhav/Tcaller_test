package com.tcall.tcall_test

import android.os.Bundle
import androidx.activity.ComponentActivity // Changed from AppCompatActivity
import androidx.activity.compose.setContent // Import for setContent
import androidx.activity.viewModels // Import for viewModels
import com.tcall.tcall_test.screens.ContentScreen // Import ContentScreen
import com.tcall.tcall_test.screens.MainScreenViewModel // Import ViewModel
import com.tcall.tcall_test.ui.theme.TCall_testTheme // Import Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() { // Changed base class

    private val mainScreenViewModel: MainScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TCall_testTheme {
                ContentScreen(viewModel = mainScreenViewModel)
            }
        }
    }
}
