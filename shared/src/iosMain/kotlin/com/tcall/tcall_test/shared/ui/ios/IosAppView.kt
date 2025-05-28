package com.tcall.tcall_test.shared.ui.ios

import androidx.compose.foundation.layout.*
import androidx.compose.material.* // Material components
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tcall.tcall_test.repository.api.NetworkService
import com.tcall.tcall_test.repository.GetDataRepository
import com.tcall.tcall_test.use_cases.GetDataUseCase
import com.tcall.tcall_test.shared.viewmodel.CommonMainScreenViewModel
// UiState is already imported by CommonMainScreenViewModel if in same package, else needs import
// import com.tcall.tcall_test.shared.viewmodel.UiState 
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@Composable
fun rememberViewModel(): CommonMainScreenViewModel {
    val scope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
    DisposableEffect(Unit) {
        onDispose {
            scope.cancel()
        }
    }

    val networkService = remember { NetworkService() } // Ktor actual for iOS
    val dataRepository = remember { GetDataRepository(networkService) }
    val useCase = remember { GetDataUseCase(dataRepository) }
    return remember { CommonMainScreenViewModel(useCase, scope) }
}

@Composable
fun IosAppView() {
    val viewModel = rememberViewModel()
    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme { // Apply MaterialTheme for consistent styling
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("KMP Data Fetcher - iOS") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Apply padding from Scaffold
                    .padding(16.dp), // Additional padding for content
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { viewModel.fetchContent() },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (uiState.isLoading) "Loading..." else "Fetch Content")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isLoading && uiState.tenthChar.contains("--")) { // Show global loading indicator only initially
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                uiState.error?.let {
                    Text(
                        text = "Error: $it",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Data display cards
                DataDisplayCard("10th Character", uiState.tenthChar, uiState.isLoading && uiState.tenthChar.contains("Loading"))
                Spacer(modifier = Modifier.height(8.dp))
                DataDisplayCard("Every 10th Character", uiState.everyTenthChar, uiState.isLoading && uiState.everyTenthChar.contains("Loading"))
                Spacer(modifier = Modifier.height(8.dp))
                DataDisplayCard("Word Count", uiState.wordCount, uiState.isLoading && uiState.wordCount.contains("Loading"))
            }
        }
    }
}

@Composable
fun DataDisplayCard(title: String, data: String, isLoading: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            if (isLoading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Loading...")
                }
            } else {
                Text(text = data, style = MaterialTheme.typography.body1)
            }
        }
    }
}
