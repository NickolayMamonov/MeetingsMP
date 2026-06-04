# KMP Architecture Rules (дополнение к AGENTS.md)

Эти правила автоматически загружаются агентами через `instructions` в opencode.json.

## Build System

- **Version catalog**: `gradle/libs.versions.toml` — единственный источник версий зависимостей
- **Convention plugins**: `gradle/conventions-plugins/` — precompiled script plugins (kotlin-dsl)
- **Configuration cache**: включён (`org.gradle.configuration-cache=true`)
- **Gradle properties**: `gradle.properties` содержит JVM args и флаги Android SDK

## Convention Plugins Reference

| Plugin ID | Modules Using It | Purpose |
|-----------|------------------|---------|
| `kmp.library.base` | all | KMP base + coroutines + serialization + detekt |
| `kmp.library.android` | meetings-sdk, composeApp, core:uikit | base + Android target |
| `kmp.library.desktop` | meetings-sdk, composeApp, core:uikit | base + JVM desktop target |
| `kmp.library.ios` | meetings-sdk, meetings-shared | base + iOS targets |
| `kmp.library.all` | composeApp, core:uikit | desktop + android + ios |
| `jetbrains-compose.base` | core:uikit, composeApp | Compose Multiplatform + lifecycle + resources |
| `jetbrains-compose.android` | composeApp | compose.base + jetpack-compose.base |
| `jetbrains-compose.ios` | meetings-shared | compose.base for iOS |
| `jetbrains-compose.desktop` | composeApp | compose.base + desktop common |
| `jetbrains-compose.all` | composeApp | android + ios + desktop compose |
| `jetpack-compose.base` | core:uikit, composeApp | Compose Compiler + Android buildFeatures |
| `android.base` | meetings-android, core:uikit | Android SDK config (compileSdk 36, minSdk 24, JDK 21) |
| `android.library` | (unused currently) | android.base + Kotlin Android |
| `detekt` | all | Detekt static analysis |
| `ktlint` | all | ktlint formatting checks |

## Key Dependencies (from libs.versions.toml)

```toml
kotlin = "2.3.0"
agp = "8.11.2"
compose-multiplatform = "1.10.0"
kotlinx-coroutines = "1.10.2"
kotlinx-serialization-json = "1.10.0"
kotlinx-datetime = "0.6.2"
ktor = "3.4.1"
ktorfit = "2.7.3"
lifecycle = "2.9.6"
detekt = "1.23.7"
```

## ViewModel Pattern

```kotlin
// In commonMain (meetings-shared or composeApp)
import org.jetbrains.androidx.lifecycle.ViewModel
import org.jetbrains.androidx.lifecycle.viewModelScope

class MyViewModel(private val repository: MyRepository) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    fun loadData() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val data = repository.getData()
                _state.value = if (data.isEmpty()) UiState.Empty else UiState.Content(data)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
```

## Ktor Client Pattern (meetings-sdk)

```kotlin
// MeetingsClient.kt in commonMain
class MeetingsClient(
    private val httpClient: HttpClient,
    private val tokenProvider: TokenProvider
) {
    // API accessors
    val auth: AuthApi get() = AuthApi(httpClient, tokenProvider)
    val communities: CommunitiesApi get() = CommunitiesApi(httpClient, tokenProvider)
}

// Per-platform engine config (actual)
// androidMain: OkHttp engine
// desktopMain: OkHttp engine  
// iosMain: Darwin engine
```

## Prohibited Patterns (will fail review)

- `android.*` import in commonMain → use expect/actual
- `java.io.File` in commonMain → use expect/actual or KMP file APIs
- `Thread.sleep()` in commonMain → use `delay()` from coroutines
- Hardcoded colors → use `MeetingsTheme.colors` or `MaterialTheme.colorScheme`
- Direct `HttpClient` usage outside meetings-sdk → use API classes
- Android `Context` in ViewModel → pass as dependency through constructor
