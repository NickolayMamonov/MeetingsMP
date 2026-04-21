import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("kmp.library.desktop")
    id("jetbrains-compose.desktop")
}

kotlin {
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
