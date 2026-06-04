---
name: kmp-android
description: Android-specific patterns for MeetingsMP — AGP config, androidMain, Compose platform entries, and Android SDK usage
license: MIT
compatibility: opencode
metadata:
  project: MeetingsMP
  platform: Android
---

## Android Config (from convention plugins)

- compileSdk: 36, minSdk: 24, targetSdk: 36
- JDK: 21
- AGP: 8.11.2
- Kotlin: 2.3.0
- ABI filters: configurable via `kmp.android.abi.filter` Gradle property

## Android Module Patterns

### Entry Point (meetings-android)
```kotlin
// MainActivity.kt — THIN entry point
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeetingsTheme {
                MeetingsApp()  // from composeApp/commonMain
            }
        }
    }
}
```

### androidMain in Shared Modules
- Platform-specific implementations via `actual` keyword
- Examples: PlatformContext, file access, permissions
- NEVER put business logic here

### Ktor Engine
- Android uses `ktor-client-okhttp`
- Desktop also uses OkHttp (separate engine config)
- iOS uses `ktor-client-darwin`

## Compose on Android

- Use `@Preview` with `MeetingsTheme` wrapper
- `collectAsStateWithLifecycle()` — available in commonMain via lifecycle-runtime-compose
- `viewModelScope` for coroutine management
- No `GlobalScope`, no `LiveData` (use Flow)

## Prohibited
- `android.*` imports in commonMain
- Hilt (Android-only DI) — use Koin for KMP
- `findViewById` — use Compose
- Platform-specific UI code outside platform modules
