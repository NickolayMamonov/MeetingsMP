import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("kmp.library.desktop")
    id("jetbrains-compose.desktop")
}

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        val desktopMain by getting
        desktopMain.dependencies {
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
