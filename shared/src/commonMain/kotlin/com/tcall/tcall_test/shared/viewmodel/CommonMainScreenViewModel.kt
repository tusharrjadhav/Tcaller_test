package com.tcall.tcall_test.shared.viewmodel

import com.tcall.tcall_test.repository.DataRepository // from shared
import com.tcall.tcall_test.use_cases.GetDataUseCase // from shared
import com.tcall.tcall_test.util.DataResult // from shared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// Helper for creating a CoroutineScope similar to viewModelScope
// In a real KMP app, you might use a library like MVIKotlin or a shared lifecycle library.
// For iOS, this scope needs to be managed appropriately.
// The ViewModel now accepts a CoroutineScope in its constructor.
// fun createViewModelScope(): CoroutineScope = CoroutineScope(Dispatchers.Main) // Dispatchers.Main needs platform actual

data class UiState(
    val tenthChar: String = "10th Char: --",
    val everyTenthChar: String = "Every 10th: --",
    val wordCount: String = "Word Count: --",
    val isLoading: Boolean = false,
    val error: String? = null
)

class CommonMainScreenViewModel(
    private val useCase: GetDataUseCase,
    private val coroutineScope: CoroutineScope // Pass scope for lifecycle management
) {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun fetchContent() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        useCase.get10thChar()
            .onEach { result ->
                _uiState.value = _uiState.value.copy(
                    tenthChar = when (result) {
                        is DataResult.Success -> "10th Char: ${result.data}"
                        is DataResult.Error -> "Error: ${result.exception.message}"
                        is DataResult.Loading -> "Loading 10th char..."
                    },
                    // Manage isLoading carefully. If multiple flows are loading,
                    // this could be set to false prematurely by another flow finishing.
                    // For simplicity, let's assume only one "isLoading" state for all.
                    // A more robust solution would track loading state per operation or combined.
                    isLoading = result is DataResult.Loading || _uiState.value.isLoading // Keep true if any are loading
                )
            }.launchIn(coroutineScope)

        useCase.getEveryTenthChar()
            .onEach { result ->
                _uiState.value = _uiState.value.copy(
                    everyTenthChar = when (result) {
                        is DataResult.Success -> "Every 10th: ${result.data}"
                        is DataResult.Error -> "Error: ${result.exception.message}"
                        is DataResult.Loading -> "Loading every 10th..."
                            },
                    isLoading = result is DataResult.Loading || _uiState.value.isLoading
                )
            }.launchIn(coroutineScope)

        useCase.getCharCount()
            .onEach { result ->
                _uiState.value = _uiState.value.copy(
                    wordCount = when (result) {
                        is DataResult.Success -> "Word Count: ${result.data}"
                        is DataResult.Error -> "Error: ${result.exception.message}"
                        is DataResult.Loading -> "Loading word count..."
                    },
                    isLoading = result is DataResult.Loading
                )
                // If this is the last one, and it's not loading, set overall isLoading to false
                if (result !is DataResult.Loading) {
                    // Check if other operations might still be loading if they were started
                    // This simple model assumes sequential or overlapping, and last one sets final state.
                    // This logic for isLoading is still basic.
                    // A robust solution would use a counter or combine flow completion.
                    // For now, if this finishes and isn't loading, we assume all are done.
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                 if (result is DataResult.Error) { // If any error occurs, stop loading
                    _uiState.value = _uiState.value.copy(isLoading = false, error = result.exception.message)
                }
            }.launchIn(coroutineScope)
    }
}
