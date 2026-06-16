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

/**
 * Configures the full KMP source set hierarchy after all targets are registered.
 *
 * Hierarchy:
 * ```
 * commonMain
 * ├── jvmMain
 * │   ├── androidMain
 * │   └── desktopMain
 * └── nativeMain
 *     └── iosMain
 *         ├── iosArm64Main
 *         ├── iosSimulatorArm64Main
 *         └── iosX64Main
 * ```
 *
 * The default hierarchy template in Kotlin 2.0+ does NOT create `jvmMain`
 * when targets are registered via separate convention plugins (due to
 * `applyIfNeeded` timing). We create it manually here.
 *
 * `nativeMain` and `iosMain` are also created manually because the default
 * hierarchy template may not run before this configuration block.
 */
afterEvaluate {
    kmpConfig {
        sourceSets {
            val commonMain by getting

            // Create jvmMain intermediate source set for shared JVM code
            val jvmMain by creating {
                dependsOn(commonMain)
            }

            val androidMain by getting
            androidMain.dependsOn(jvmMain)

            val desktopMain by getting
            desktopMain.dependsOn(jvmMain)

            // Create nativeMain intermediate source set for shared native code
            val nativeMain by creating {
                dependsOn(commonMain)
            }

            // Create iosMain and connect it to nativeMain
            // Use maybeCreate because iosMain may already exist (default hierarchy template)
            // or may not exist yet (if template hasn't run)
            val iosMain = sourceSets.maybeCreate("iosMain")
            iosMain.dependsOn(nativeMain)

            // Connect iOS leaf source sets to iosMain
            // These are created by the ios() target registration
            sourceSets.findByName("iosArm64Main")?.dependsOn(iosMain)
            sourceSets.findByName("iosSimulatorArm64Main")?.dependsOn(iosMain)
            sourceSets.findByName("iosX64Main")?.dependsOn(iosMain)
        }
    }
}