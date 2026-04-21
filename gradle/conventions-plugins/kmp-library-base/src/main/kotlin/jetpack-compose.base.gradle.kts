import dev.whysoezzy.meetings.gradle.kmplibbase.androidConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.libs

plugins.applyIfNeeded(libs.plugins.jetbrains.compose.compiler.get().pluginId)

androidConfig {
    buildFeatures {
        compose = true
    }
}