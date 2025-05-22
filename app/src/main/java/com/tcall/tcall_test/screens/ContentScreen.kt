package com.tcall.tcall_test.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tcall.tcall_test.R

private enum class ErrorSource {
    TENTH_CHAR,
    EVERY_10TH_CHAR,
    CHAR_COUNT
}

@Composable
fun ContentScreen(viewModel: MainScreenViewModel) {
    // Observe the State objects from the ViewModel
    val tenthCharState by viewModel.tenthChar
    val every10thCharState by viewModel.every10thChar
    val charCountState by viewModel.charCount

    var shownErrorDialogContent by remember { mutableStateOf<Pair<ErrorSource, String>?>(null) }

    // LaunchedEffect to monitor errors from ViewModel and show dialog
    LaunchedEffect(tenthCharState.error, every10thCharState.error, charCountState.error) {
        if (shownErrorDialogContent == null) { // Only show if no dialog is currently up
            when {
                tenthCharState.error.isNotEmpty() -> {
                    shownErrorDialogContent = Pair(ErrorSource.TENTH_CHAR, tenthCharState.error)
                }
                every10thCharState.error.isNotEmpty() -> {
                    shownErrorDialogContent = Pair(ErrorSource.EVERY_10TH_CHAR, every10thCharState.error)
                }
                charCountState.error.isNotEmpty() -> {
                    shownErrorDialogContent = Pair(ErrorSource.CHAR_COUNT, charCountState.error)
                }
            }
        }
    }

    if (shownErrorDialogContent != null) {
        val (errorSource, errorMessage) = shownErrorDialogContent!!
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog and clear the specific error that was shown
                when (errorSource) {
                    ErrorSource.TENTH_CHAR -> viewModel.dismissTenthCharError()
                    ErrorSource.EVERY_10TH_CHAR -> viewModel.dismissEvery10thCharError()
                    ErrorSource.CHAR_COUNT -> viewModel.dismissCharCountError()
                }
                shownErrorDialogContent = null
            },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = {
                    when (errorSource) {
                        ErrorSource.TENTH_CHAR -> viewModel.dismissTenthCharError()
                        ErrorSource.EVERY_10TH_CHAR -> viewModel.dismissEvery10thCharError()
                        ErrorSource.CHAR_COUNT -> viewModel.dismissCharCountError()
                    }
                    shownErrorDialogContent = null
                }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.fetchContent() },
            modifier = Modifier.testTag("FetchDataButton")
        ) {
            Text(stringResource(id = R.string.fetch_data))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "10th Character: ${
            tenthCharState.data ?: (if (tenthCharState.loading) "Loading..." else "N/A")
        }")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Word Count: ${
            charCountState.data ?: (if (charCountState.loading) "Loading..." else "N/A")
        }")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Every 10th Character: ${
            every10thCharState.data ?: (if (every10thCharState.loading) "Loading..." else "N/A")
        }")
    }
}
