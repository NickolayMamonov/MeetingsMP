import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.kmpConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.libs

plugins.applyIfNeeded(libs.plugins.jetbrains.kotlin.multiplatform.get().pluginId)

kmpConfig {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.annotation)
            implementation(libs.kotlinx.datetime)
        }
    }
}

