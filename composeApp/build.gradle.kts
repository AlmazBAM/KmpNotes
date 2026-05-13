import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        androidMain.dependencies {
            // Android-specific UI & Tooling
            implementation(libs.androidx.activity.compose)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.ui.tooling)

            // Android-specific DI
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }

        commonMain.dependencies {
            // Compose UI & Foundation
            implementation(libs.bundles.compose)

            // Lifecycle & Navigation
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.navigation.compose)

            // DI
            api(libs.bundles.koin)

            // Data & Serialization
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.bundles.coil)
            implementation(libs.sqlite.bundled)
            implementation(libs.androidx.room.runtime)
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    dependencies {
        ksp(libs.androidx.room.compiler)
    }
}

android {
    namespace = "com.bagmanov.kmpnotes"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.bagmanov.kmpnotes"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}

