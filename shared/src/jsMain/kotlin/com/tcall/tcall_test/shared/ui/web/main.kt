package com.tcall.tcall_test.shared.ui.web

import org.jetbrains.compose.web.renderComposable
// Import the main Composable UI for the web app
import com.tcall.tcall_test.shared.ui.web.WebAppView // Added import

fun main() {
    renderComposable(rootElementId = "root") {
        WebAppView() // Call the main Composable for the web app
    }
}
