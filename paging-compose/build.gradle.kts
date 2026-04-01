plugins {
    alias(libs.plugins.androidLibrary)
    // Замените kotlinAndroid на multiplatform, так как это KMP модуль
    kotlin("multiplatform")
    alias(libs.plugins.composeCompiler)
    id("org.jetbrains.compose") // Add this
    `maven-publish`
}

kotlin {
    androidTarget { publishLibraryVariants("release") }
    jvm("desktop")

    // Apple\
    /*
    iosX64(); iosArm64(); iosSimulatorArm64()
    macosX64(); macosArm64()
    watchosX64(); watchosArm64(); watchosSimulatorArm64()
    tvosX64(); tvosArm64(); tvosSimulatorArm64()
    */

    // Linux
    linuxX64()
    linuxArm64() // Добавили ARM64

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":paging-common"))
                api(compose.runtime)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                // This is required for AndroidUiDispatcher
                implementation("androidx.compose.ui:ui:1.7.0")
            }
        }

        // Базовый слой для всего, что не Android
        val nonAndroidMain by creating {
            dependsOn(commonMain)
        }

        // Общий слой для Linux (x64 + Arm64)
        val linuxMain by creating {
            dependsOn(nonAndroidMain)
        }

        // Привязываем конкретные линуксы к общему linuxMain
        val linuxX64Main by getting { dependsOn(linuxMain) }
        val linuxArm64Main by getting { dependsOn(linuxMain) }

        val desktopMain by getting { dependsOn(nonAndroidMain) }

        // Связываем Apple таргеты с nonAndroidMain (опционально, если нужно)
        //val appleMain by creating { dependsOn(nonAndroidMain) }
        // В Kotlin 1.9.20+ iosMain, macosMain и т.д. создаются автоматически,

    }
}

android {
    // Данные из блока androidLibrary
    namespace = "androidx.paging.compose"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}

group = project.property("GROUP") as String
version = project.property("VERSION_NAME") as String
// Настройка публикации в Maven
publishing {
    repositories {
        mavenLocal()
    }
}

// Блок 'androidx { ... }' полностью удаляем или заменяем на стандартную публикацию Maven,
// так как это кастомное расширение Google для их репозитория.
