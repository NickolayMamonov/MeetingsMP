import dev.whysoezzy.meetings.gradle.kmplibbase.androidConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.config.IOSPlatform
import dev.whysoezzy.meetings.gradle.kmplibbase.jvmTarget
import dev.whysoezzy.meetings.gradle.kmplibbase.kmpConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.config.kmpIosPlatforms
import dev.whysoezzy.meetings.gradle.kmplibbase.libs

plugins.applyIfNeeded(libs.plugins.jetbrains.kotlin.multiplatform.get().pluginId)
plugins.applyIfNeeded("kmp.library.base")

kmpConfig {
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(libs.jvmTarget())
        }
    }

    sourceSets {
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}
