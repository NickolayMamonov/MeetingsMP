import dev.whysoezzy.meetings.gradle.kmplibbase.androidConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.config.IOSPlatform
import dev.whysoezzy.meetings.gradle.kmplibbase.jvmTarget
import dev.whysoezzy.meetings.gradle.kmplibbase.kmpConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.config.kmpIosPlatforms
import dev.whysoezzy.meetings.gradle.kmplibbase.libs


plugins.applyIfNeeded(libs.plugins.jetbrains.kotlin.multiplatform.get().pluginId)
plugins.applyIfNeeded("kmp.library.base")

val iosFrameworkBaseNameOverride = providers
    .gradleProperty("kmp.ios.framework.baseName")
    .orNull
val iosFrameworkBaseName = (iosFrameworkBaseNameOverride ?: project.name).toSwiftModuleName()

kmpConfig {
    kmpIosPlatforms.asSequence()
        .map {
            when (it) {
                IOSPlatform.ARM_64 -> iosArm64()
                IOSPlatform.SIMULATOR_ARM64 -> iosSimulatorArm64()
                IOSPlatform.SIMULATOR_X64 -> iosX64()
            }
        }.forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = iosFrameworkBaseName
                isStatic = true
            }
        }
}

private fun String.toSwiftModuleName(): String = this
    .replace(Regex("[^A-Za-z0-9_]"), "_")
    .replace(Regex("^[^A-Za-z_]"), "_")
    .ifBlank { "MeetingsFramework" }
