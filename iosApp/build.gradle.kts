import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework // May not be needed if not creating separate XCFramework from iosApp

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    iosSimulatorArm64("ios") { // "ios" is the target name, can be anything
        binaries.executable {
            // The entry point for the Kotlin/Native application.
            // This function should initialize your Compose UI.
            // e.g., entryPoint = "com.tcall.tcall_test.ios.main"
            // This needs a corresponding Kotlin function:
            // package com.tcall.tcall_test.ios
            // fun main() { ... launch Compose UI ... }
        }
    }

    sourceSets {
        val iosMain by getting { // iosApp's own main source set for this target
            dependencies {
                implementation(project(":shared"))
                // Other iOS specific dependencies for iosApp if any
            }
        }
    }
}

// Compose for iOS specific settings (usually handled by shared module)
compose {
    // No specific desktop/web settings here
}
