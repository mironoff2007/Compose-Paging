plugins {
    kotlin("multiplatform") version "2.3.20" // используйте вашу версию
    id("com.android.library") version "8.11.2"
}

kotlin {
    // 1. Настройка таргетов (аналог макросов из androidx)
    androidTarget {
        publishLibraryVariants("release")
    }
    jvm("desktop")

    // Apple / Native
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    watchosX64()
    watchosArm64()
    watchosSimulatorArm64()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
    macosX64()
    macosArm64()

    // Desktop / Web
    linuxX64()
    mingwX64()
    js(IR) { browser() }
    wasmJs() { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                api("androidx.annotation:annotation:1.9.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
            }
        }

        // Аналог jvmAndAndroid
        val jvmAndAndroidMain = create("jvmAndAndroidMain") {
            dependsOn(commonMain)
            dependencies {
                api("androidx.arch.core:core-common:2.2.0")
            }
        }

        val androidMain by getting { dependsOn(jvmAndAndroidMain) }
        val desktopMain by getting { dependsOn(jvmAndAndroidMain) }

        // Native / Non-JVM
        val nonJvmMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:atomicfu:0.23.1")
            }
        }

        // Настройка иерархии для Native (Unix, Apple и т.д.)
        // В новых версиях Kotlin (1.9+) иерархия создается автоматически (Default Hierarchy Template)
    }
}

android {
    namespace = "androidx.paging.common"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}