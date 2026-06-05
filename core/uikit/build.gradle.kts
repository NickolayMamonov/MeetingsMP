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
            api(project(":meetings-sdk"))
        }

        @Suppress("unused")
        val desktopTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlin.test.junit)
            }
        }
    }
}

android {
    namespace = "dev.whysoezzy.meetings.compose"
}
