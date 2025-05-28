import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library") // For Android library specific configurations
    id("org.jetbrains.compose") version "1.2.1"
}

kotlin {
    android {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        browser()
        binaries.executable() // Ensure JS binaries are built
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1") // Aligned with project
                implementation("io.ktor:ktor-client-core:2.3.0") // Added Ktor core for commonMain
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.7.0")
                implementation("androidx.appcompat:appcompat:1.3.0") // Kept as per instruction
                implementation("androidx.compose.ui:ui-tooling-preview:1.1.1") // ext.compose_ui_version
                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            // iOS specific dependencies can be added here
            dependencies { // Added dependencies block for iosMain
                implementation("io.ktor:ktor-client-darwin:2.3.0") // Added Ktor Darwin engine for iosMain
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.html.core) // Compose for Web core
                implementation(compose.html.material) // For Material Design components on Web
                implementation("io.ktor:ktor-client-js:2.3.0") // Ktor JS engine
                // implementation(compose.html.widgets) // Example: if using pre-built widgets for web
            }
        }
        // Test source sets can be configured similarly if needed
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1") // For testing coroutines
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2") // Align with project's junitVersion
            }
        }
        val iosTest by creating { // For iOS tests
            dependsOn(commonTest)
            // iOS specific test dependencies
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }

    // Framework linkage for iOS targets
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared" // This will be the name of the framework
            // You can export specific APIs if needed, e.g.:
            // export("com.tcall.tcall_test.repository.api.NetworkService")
            // export("com.tcall.tcall_test.use_cases.GetDataUseCase")
        }
    }
}

android {
    namespace = "com.tcall.tcall_test.shared"
    compileSdk = 32
    defaultConfig {
        minSdk = 24
    }
    // It's good practice to set source compatibility here as well for the Android library
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // lintOptions {
    //    abortOnError = false
    // }
}
