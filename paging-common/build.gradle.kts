plugins {
    kotlin("multiplatform") version "2.3.20" // используйте вашу версию
    id("com.android.library") version "8.11.2"
    `maven-publish`
}

kotlin {
    // 1. Настройка таргетов (аналог макросов из androidx)
    androidTarget {
        publishLibraryVariants("release")
    }
    jvm("desktop")

    // Apple / Native
    //iosX64()
    //iosArm64()
    //iosSimulatorArm64()
    //watchosX64()
    //watchosArm64()
    //watchosSimulatorArm64()
    ///tvosX64()
    //tvosArm64()
    //tvosSimulatorArm64()
    //macosX64()
    //macosArm64()

    // Desktop / Web
    linuxX64()
    linuxArm64() // Добавляем ARM64

    //mingwX64()
    //js(IR) { browser() }
    //wasmJs() { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                api("androidx.annotation:annotation:1.9.1")
            }
        }

        // 1. Общий слой для всего, что не JVM (Native, JS и т.д.)
        val nonJvmMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:atomicfu:0.23.1")
            }
        }

        val nativeMain by creating {
            dependsOn(nonJvmMain)
        }

        // Link Linux targets to nativeMain
        val linuxMain by creating {
            dependsOn(nativeMain)
        }

        val linuxX64Main by getting { dependsOn(linuxMain) }
        val linuxArm64Main by getting { dependsOn(linuxMain) }

        // JVM / Android
        val jvmAndAndroidMain = create("jvmAndAndroidMain") {
            dependsOn(commonMain)
            dependencies {
                api("androidx.arch.core:core-common:2.2.0")
            }
        }
        val androidMain by getting { dependsOn(jvmAndAndroidMain) }
        val desktopMain by getting { dependsOn(jvmAndAndroidMain) }
    }
}

android {
    namespace = "androidx.paging.common"
    compileSdk = 34
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