plugins {
    id("kmp.library.all")
    id("jetbrains-compose.all")
    alias(libs.plugins.jetbrains.compose.hot.reload)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.uikit)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "dev.whysoezzy.meetings.compose"
}
