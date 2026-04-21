import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.composeExt
import dev.whysoezzy.meetings.gradle.kmplibbase.kmpConfig

plugins.applyIfNeeded("jetbrains-compose.base")

val composeExtension = composeExt

kmpConfig {
    sourceSets.matching { it.name == "desktopMain" }.configureEach {
        dependencies {
            implementation(composeExtension.dependencies.desktop.common)
            implementation(composeExtension.dependencies.desktop.currentOs)
        }
    }
}
