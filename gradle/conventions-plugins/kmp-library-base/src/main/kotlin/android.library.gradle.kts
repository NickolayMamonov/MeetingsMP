import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.javaVersion
import dev.whysoezzy.meetings.gradle.kmplibbase.jvmTarget
import dev.whysoezzy.meetings.gradle.kmplibbase.kotlinJvmCompilerOptions
import dev.whysoezzy.meetings.gradle.kmplibbase.libs

plugins.apply("android.base")
if (plugins.hasPlugin(libs.plugins.jetbrains.kotlin.multiplatform.get().pluginId)) {
    plugins.applyIfNeeded(libs.plugins.jetbrains.kotlin.android.get().pluginId)
}

project.dependencies {
    "implementation"(libs.kotlinx.coroutines.android)
    "implementation"(libs.androidx.core)
    "implementation"(libs.androidx.annotation)
}

kotlinJvmCompilerOptions {
    jvmTarget.set(libs.jvmTarget())
    freeCompilerArgs.add("-Xjdk-release=${libs.javaVersion()}")
}