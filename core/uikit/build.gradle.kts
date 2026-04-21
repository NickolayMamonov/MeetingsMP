plugins {
    id("kmp.library.all")
    id("jetpack-compose.base")
    alias(libs.plugins.jetbrains.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.ui)
        }
    }
}

android {
    namespace = "dev.whysoezzy.meetings.compose"
}
