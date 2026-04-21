package dev.whysoezzy.meetings.gradle.kmplibbase.config

import org.gradle.api.Project

internal val supportedIOSTargetsParam = ProjectConfigParam(
    cmdParamName = "kmp.ios.platforms",
    androidLocalPropertyParamName = "kmp.ios.platforms",
    envParamName = "KMP_IOS_PLATFORMS",
    defaultValue = "arm64,simulatorArm64,simulatorIntelX64"
)

enum class IOSPlatform(val value: String) {
    ARM_64("arm64"),
    SIMULATOR_ARM64("simulatorArm64"),
    SIMULATOR_X64("simulatorIntelX64"),
}

val Project.kmpIosPlatforms: List<IOSPlatform>
    get() {
        val allowedValues = IOSPlatform.values().map { it.value }
        return readConfigParam(supportedIOSTargetsParam)
            ?.split(",")
            ?.map { it.trim() }
            ?.map { paramValue ->
                IOSPlatform.values().firstOrNull { it.value == paramValue }
                    ?: throw IllegalArgumentException(
                        "Unsupported iOS platform '$paramValue' for '${supportedIOSTargetsParam.cmdParamName}'. " +
                            "Allowed values: ${allowedValues.joinToString(", ")}"
                    )
            }
            ?: IOSPlatform.values().toList()
    }