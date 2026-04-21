import dev.whysoezzy.meetings.gradle.kmplibbase.androidConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.javaVersion
import dev.whysoezzy.meetings.gradle.kmplibbase.libs
import dev.whysoezzy.meetings.gradle.kmplibbase.config.requestedAndroidAbis

androidConfig {
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        ndk {
            requestedAndroidAbis.takeUnless { it.isNullOrEmpty() }?.let { abis: List<String> ->
                abiFilters.addAll(abis)
            }
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        val javaVersion = libs.javaVersion()
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}
