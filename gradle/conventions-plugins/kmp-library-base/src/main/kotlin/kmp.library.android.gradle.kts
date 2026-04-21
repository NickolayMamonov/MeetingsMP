import dev.whysoezzy.meetings.gradle.kmplibbase.androidConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.config.IOSPlatform
import dev.whysoezzy.meetings.gradle.kmplibbase.jvmTarget
import dev.whysoezzy.meetings.gradle.kmplibbase.kmpConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.config.kmpIosPlatforms
import dev.whysoezzy.meetings.gradle.kmplibbase.libs

plugins.applyIfNeeded(libs.plugins.jetbrains.kotlin.multiplatform.get().pluginId)
plugins.applyIfNeeded(libs.plugins.android.library.get().pluginId)
plugins.applyIfNeeded("kmp.library.base")

kmpConfig {
    androidTarget {
        compilerOptions {
            jvmTarget.set(libs.jvmTarget())
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core)
            implementation(libs.kotlinx.coroutines.android)
        }
    }
}

androidConfig {
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
    }
}

plugins.apply("android.base")