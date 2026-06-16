plugins {
    id("kmp.library.all")
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.ktorfit)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktorfit)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.koin.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.multiplatform.settings.test)
        }
    }
}

// Source sets created by the convention plugin's afterEvaluate block
// (jvmMain, iosMain, desktopTest) are only available after the hierarchy
// is configured. We use afterEvaluate here to access them.
afterEvaluate {
    kotlin {
        sourceSets {
            val jvmMain by getting
            jvmMain.dependencies {
                implementation(libs.ktor.client.engine.okhttp)
            }

            val iosMain by getting
            iosMain.dependencies {
                implementation(libs.ktor.client.engine.darwin)
            }

            val desktopTest by getting
            desktopTest.dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

android {
    namespace = "dev.whysoezzy.meetingssdk"
}
