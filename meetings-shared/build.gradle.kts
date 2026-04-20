plugins {
    id("kmp.library.ios")
    id("jetbrains-compose.ios")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Необходимо явно добавить все зависимости для работы
            // приложения на iOS, чтобы они попали в XCFramework
            implementation(projects.composeApp)
        }
    }
}
