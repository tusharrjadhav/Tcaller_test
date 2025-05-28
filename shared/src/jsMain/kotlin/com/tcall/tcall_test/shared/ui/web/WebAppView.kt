package com.tcall.tcall_test.shared.ui.web

import androidx.compose.runtime.*
import com.tcall.tcall_test.repository.api.NetworkService // Will need JS actual for NetworkService
import com.tcall.tcall_test.repository.GetDataRepository
import com.tcall.tcall_test.use_cases.GetDataUseCase
import com.tcall.tcall_test.shared.viewmodel.CommonMainScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.jetbrains.compose.web.css.* // For styling
import org.jetbrains.compose.web.dom.* // Standard DOM elements (Div, P, Button etc.)
import org.jetbrains.compose.web.material.* // Material components for Web (Button, Card, etc.)

// ViewModel instantiation helper for JS
@Composable
fun rememberWebViewModel(): CommonMainScreenViewModel {
    val scope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Main) } // Dispatchers.Main for JS
    DisposableEffect(Unit) {
        onDispose {
            scope.cancel()
        }
    }

    // Instantiate actual dependencies for JS
    val networkService = NetworkService() // Resolves to Ktor JS actual
    val dataRepository = GetDataRepository(networkService) // GetDataRepository from commonMain
    val useCase = GetDataUseCase(dataRepository)
    // --- End Temporary Workaround / Actual dependencies are now used ---

    return remember { CommonMainScreenViewModel(useCase, scope) }
}

@Composable
fun WebAppView() {
    val viewModel = rememberWebViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Apply some basic styling for centering and layout
    Style(WebAppStyleSheet)

    Div({ classes(WebAppStyleSheet.appContainer) }) {
        H1 { Text("KMP Data Fetcher - Web") }

        MButton( // Material Button
            onClick = { viewModel.fetchContent() },
            attrs = {
                if (uiState.isLoading) {
                    classes(WebAppStyleSheet.buttonDisabled) // Example of disabling via class
                    // disabled() // Standard HTML disabled attribute
                }
            }
        ) {
            Text(if (uiState.isLoading) "Loading..." else "Fetch Content")
        }

        if (uiState.isLoading && uiState.tenthChar.contains("--")) {
            Div({ style { marginTop(16.px) } }) {
                // CircularProgressIndicator is not standard in compose.html.material directly
                // We can use a text placeholder or a custom CSS spinner
                Text("Loading data globally...")
            }
        }

        uiState.error?.let {
            P({ style { color(Color.red) } }) { Text("Error: $it") }
        }

        // Data display sections
        DataDisplayWebContent("10th Character", uiState.tenthChar, uiState.isLoading && uiState.tenthChar.contains("Loading"))
        DataDisplayWebContent("Every 10th Character", uiState.everyTenthChar, uiState.isLoading && uiState.everyTenthChar.contains("Loading"))
        DataDisplayWebContent("Word Count", uiState.wordCount, uiState.isLoading && uiState.wordCount.contains("Loading"))
    }
}

@Composable
fun DataDisplayWebContent(title: String, data: String, isLoading: Boolean) {
    Div({ classes(WebAppStyleSheet.card) }) {
        H2({ classes(WebAppStyleSheet.cardTitle) }) { Text(title) }
        if (isLoading) {
            P { Text("Loading...") }
        } else {
            P { Text(data) }
        }
    }
}

// Basic StyleSheet for the WebApp
object WebAppStyleSheet : StyleSheet() {
    val appContainer by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        alignItems(AlignItems.Center)
        padding(20.px)
        fontFamily("Arial, Helvetica, sans-serif")
    }

    val card by style {
        width(300.px)
        padding(15.px)
        margin(10.px)
        property("border", "1px solid #ccc")
        property("border-radius", "8px")
        property("box-shadow", "2px 2px 8px rgba(0,0,0,0.1)")
    }

    val cardTitle by style {
        fontSize(1.2.em)
        marginBottom(8.px)
    }
    
    val buttonDisabled by style {
        opacity(0.6)
        property("pointer-events", "none")
    }
}
