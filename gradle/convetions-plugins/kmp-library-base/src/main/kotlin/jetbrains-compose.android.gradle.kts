import dev.whysoezzy.meetings.gradle.kmplibbase.androidConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.composeExt
import dev.whysoezzy.meetings.gradle.kmplibbase.kmpConfig

plugins.applyIfNeeded("jetbrains-compose.base")
plugins.applyIfNeeded("jetpack-compose.base")

kmpConfig {
    sourceSets {
        androidMain.dependencies {
            implementation(composeExt.dependencies.preview)
        }
    }
}

dependencies {
    "debugImplementation"(composeExt.dependencies.uiTooling)
}