import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose.compiler)
    alias(libs.plugins.jetbrains.compose.multiplatform)
}


kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        val desktopMain  by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(projects.composeApp)
        }
    }

}

compose.desktop {
    application {
        mainClass = "dev.whysoezzy.meetings.Meetings"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.whysoezzy.meetings"
            packageVersion = "1.0.0"
        }
    }
}