plugins {
    alias(libs.plugins.androidLibrary)
    // Замените kotlinAndroid на multiplatform, так как это KMP модуль
    kotlin("multiplatform")
    alias(libs.plugins.composeCompiler)
    id("org.jetbrains.compose") // Add this
}

kotlin {
    // 1. Объявляем таргеты вручную (замена androidXMultiplatform)
    androidTarget {
        publishLibraryVariants("release")
    }
    jvm("desktop") // Desktop (JVM)

    // Apple
    iosX64(); iosArm64(); iosSimulatorArm64()
    macosX64(); macosArm64()
    watchosX64(); watchosArm64(); watchosSimulatorArm64()
    tvosX64(); tvosArm64(); tvosSimulatorArm64()

    // Other
    linuxX64()



    // 2. Настройка SourceSets
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":paging-common"))
                // Используйте зависимости из вашего libs.versions.toml если они там есть
                api(compose.runtime)
            }
        }

        val commonTest by getting {
            dependencies {
                // Если internal-testutils-paging не переписан под стандартный KMP,
                // он может вызвать ошибки компиляции
                //implementation(project(":internal-testutils-paging"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("androidx.compose.ui:ui:1.7.0")
            }
        }

        // Создаем иерархию nonAndroidMain
        val nonAndroidMain by creating {
            dependsOn(commonMain)
        }

        val desktopMain by getting { dependsOn(nonAndroidMain) }



        // Привязываем остальные таргеты к nonJvmMain (упрощенно)
        // В Kotlin 1.9.20+ большая часть иерархии строится автоматически
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

// Блок 'androidx { ... }' полностью удаляем или заменяем на стандартную публикацию Maven,
// так как это кастомное расширение Google для их репозитория.
