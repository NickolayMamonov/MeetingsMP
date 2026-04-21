plugins {
    id("kmp.library.all")
    id("jetbrains-compose.all")
    alias(libs.plugins.jetbrains.compose.hot.reload)
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "dev.whysoezzy.meetings.compose"
}
