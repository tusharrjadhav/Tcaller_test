package com.tcall.tcall_test.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    LaunchedEffect(Unit, block = {
        viewModel.get10thCharacterValue()
        viewModel.getEveryTenthCharacter()
        viewModel.getCharCount()
    })

    val lazyListState = rememberLazyListState()

    /*LazyColumn(modifier = Modifier
        .padding(15.dp)
        .fillMaxWidth(), state = lazyListState ){
        item {
            if (viewModel.tenthChar.value.error.isEmpty()) {
                Box(modifier = Modifier) {
                    Text(
                        text = viewModel.get10thChar.toString(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            if (viewModel.every10thChar.value.error.isEmpty()) {
                Box() {
                    Text(
                        text = viewModel.getEvery10thCharResponse.toString(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            if (viewModel.charCount.value.error.isEmpty()) {
                Box() {
                    Text(
                        text = viewModel.getWordCountResponse.toString(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }*/
}