plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose.multiplatform)
    alias(libs.plugins.jetbrains.compose.compiler)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "MeetingsShared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.composeApp)
            // Необходимо явно добавить все зависимости для работы
            // приложения на iOS, чтобы они попали в XCFramework
            implementation(compose.runtime)
            implementation(compose.ui)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.composeApp)
        }
    }
}
