package dev.whysoezzy.meetings.gradle.kmplibbase.config

import org.gradle.api.Project

internal val requestedAndroidAbisParam = ProjectConfigParam(
    cmdParamName = "android.ndk.abis",
    androidLocalPropertyParamName = "android.ndk.abis",
    envParamName = "ANDROID_NDK_ABIS"
)

val Project.requestedAndroidAbis: List<String>?
    get() {
        return readConfigParam(requestedAndroidAbisParam)
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
    }