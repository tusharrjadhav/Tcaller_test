package com.tcall.tcall_test.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tcall.tcall_test.R

@Composable
fun ContentScreen(viewModel: MainScreenViewModel) {
    // Observe the State objects from the ViewModel
    val tenthCharState by viewModel.tenthChar
    val every10thCharState by viewModel.every10thChar
    val charCountState by viewModel.charCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.fetchContent() }) {
            Text(stringResource(id = R.string.fetch_data))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display data from State objects
        // Show data if available, otherwise show error, otherwise show loading or empty
        Text(text = "10th Character: ${
            tenthCharState.data ?: tenthCharState.error ?: (if (tenthCharState.loading) "Loading..." else "")
        }")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Word Count: ${
            charCountState.data ?: charCountState.error ?: (if (charCountState.loading) "Loading..." else "")
        }")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Every 10th Character: ${
            every10thCharState.data ?: every10thCharState.error ?: (if (every10thCharState.loading) "Loading..." else "")
        }")
    }
}
