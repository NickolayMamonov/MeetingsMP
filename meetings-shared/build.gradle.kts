plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)

}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        commonMain.dependencies {
            api(projects.composeApp)
        }
    }
}
