import dev.whysoezzy.meetings.gradle.kmplibbase.androidConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.config.IOSPlatform
import dev.whysoezzy.meetings.gradle.kmplibbase.jvmTarget
import dev.whysoezzy.meetings.gradle.kmplibbase.kmpConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.config.kmpIosPlatforms
import dev.whysoezzy.meetings.gradle.kmplibbase.libs

plugins.applyIfNeeded("kmp.library.desktop")
plugins.applyIfNeeded("kmp.library.android")
plugins.applyIfNeeded("kmp.library.ios")
