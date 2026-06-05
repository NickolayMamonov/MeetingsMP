plugins {
    id("kmp.library.all")
    id("jetbrains-compose.all")
    alias(libs.plugins.jetbrains.compose.hot.reload)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":meetings-sdk"))
            implementation(project(":core:uikit"))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
            implementation(libs.androidx.lifecycle.viewmodel)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.test)
        }
    }
}

android {
    namespace = "dev.whysoezzy.meetings.compose"
}
