---
type: implementation-plan
slug: meeting-to-kmp-migration
title: Доработка перенесённого кода Meeting → MeetingsMP (исправление блокеров)
status: ready-for-implementation
review_verdict_cycle1: FAIL (7 blockers — resolved)
review_verdict_cycle2: FAIL (4 new blockers — resolved)
review_verdict_cycle3: SPLIT (2 READY / 1 NOT READY → 5 KMP fixes applied)
---

# План доработки: Meeting → MeetingsMP (исправление критических блокеров)

## Статус на момент плана
Код из Meeting Android **уже перенесён** в MeetingsMP по стратегии, соответствующей AGENTS.md §5a:
- `meetings-sdk/src/commonMain/` — domain-модели, DTO, мапперы, UseCases, Repository-интерфейсы,
  API-интерфейсы, auth-логика, network-слой (Ktor + ktorfit)
- `composeApp/src/commonMain/` — ViewModel-ы, Composable-экраны, навигация (`MeetNavController`),
  DI-модули Koin
- `core:uikit/src/commonMain/` — общие UI-компоненты

Перенос выполнен структурно верно (таблица пакетов §5a соблюдена), но содержит
функциональные дефекты, которые этот план устраняет.

---

## Что конкретно надо исправить (scope)

### A. Контракт MeetingDto (§9 — нерушимо)

В `MeetingDto` отсутствуют поля, обязательные по §9:

| Поле | Тип | Дефолт | Назначение |
|------|-----|--------|------------|
| `source` | `String` | `"INTERNAL"` | отличать TIMEPAD от внутренних событий |
| `externalUrl` | `String?` | `null` | внешняя регистрация для TIMEPAD |
| `isOnline` | `Boolean` | `false` | скрывать карту для онлайн-событий |

Все поля добавляются **аддитивно** (с дефолтами — обратная совместимость при десериализации).

Дополнительно в доменную модель `Meeting` добавить вычисляемое свойство:
```kotlin
val hasLocation: Boolean get() = !isOnline && address.latitude != 0.0 && address.longitude != 0.0
```
UI использует `hasLocation` для видимости блока карты (§9).

Обновить: `MeetingDto` (meetings-sdk/.../dto/), `Meeting` (meetings-sdk/.../domain/models/),
`MeetingDtoMapper`, тесты маппера с TIMEPAD-данными (пустые host/participants, capacity=0).

**Риск десериализации:** kotlinx.serialization с `encodeDefaults = false` (текущая настройка
`MeetingsClient`) корректно обрабатывает отсутствующие поля с дефолтами при приёме.
При отправке на сервер новые поля не попадут в JSON, если равны дефолтам —
это ожидаемое поведение (новые поля только для десериализации).

### B. Аутентификация (§9 — OTP flow)

**Текущие проблемы и их исправление:**

**B.1 — AuthResponse: единый токен → access + refresh**

Заменить `AuthResponse`:
```kotlin
@Serializable
data class AuthResponse(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    val user: UserDto,
    val isNewUser: Boolean = false,
    val isRecovered: Boolean = false,
)
```
**Важно:** изменение имён полей с `token` на `accessToken`/`refreshToken` требует синхронизации
с бэкендом. Если бэкенд пока возвращает `token`, использовать `@SerialName("token")` для `accessToken`
и дефолтный `refreshToken` (или временный adapter). Явно согласовать с бэкендом до мержа.

Ввести отдельный DTO для refresh-ответа (чтобы не тащить `user`/`isNewUser` в каждом refresh):
```kotlin
@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
```

Обновить `AuthApi`:
```kotlin
suspend fun refreshToken(refreshToken: String): RefreshTokenResponse
```

**B.2 — RefreshTokenBody: access вместо refresh**

Заменить `RefreshTokenBody(currentToken.token)` на `RefreshTokenBody(currentToken.refreshToken)`.
`RefreshTokenBody` должен отправлять **refresh-токен**, а не access-токен.

**B.3 — Ktor bearer plugin: отсутствует блок `refreshTokens`**

В `defaultHttpClient` добавить блок `refreshTokens`:
```kotlin
bearer {
    loadTokens {
        val token = tokenProvider.getToken() ?: return@loadTokens null
        BearerTokens(token.accessToken, token.refreshToken)
    }
    refreshTokens {
        val oldTokens = call.response.request.bearerAuthTokens ?: return@refreshTokens null
        val response = auth.refreshToken(oldTokens.refreshToken)
        val newTokens = BearerTokens(response.accessToken, response.refreshToken)
        tokenProvider.saveTokens(AuthTokenPair(response.accessToken, response.refreshToken))
        newTokens
    }
    sendWithoutRequest { request -> request.url.host == expectedApiHost }
}
```

**B.4 — Обработка неудачного refresh**

При 401 на `/auth/refresh`:
1. Вызвать `tokenProvider.clearTokens()`
2. `TokenProvider.isLoggedIn` (StateFlow) автоматически переходит в `false` при очистке токенов
3. `AuthRepositoryImpl` делегирует свой `_isLoggedInFlow` этому flow — UI реагирует навигацией на экран авторизации

Реализовать в `refreshTokens` блоке через try/catch с очисткой токенов при ошибке.
Связка: `refreshTokens (MeetingsClient) → tokenProvider.clearTokens() → TokenProvider.isLoggedIn: StateFlow → AuthRepositoryImpl → UI`.

**B.5 — Endpoint-ы: согласование с контрактом §9**

Текущие `auth/request-code` и `auth/verify-code` → целевые `auth/send-otp` и `auth/verify-otp`.
**Важно:** смена endpoint-ов требует синхронизации с бэкендом. Добавить конфигурируемость:
- Если бэкенд уже поддерживает новые пути — сменить сразу
- Если нет — оставить старые, вынести endpoint-префиксы в параметры `MeetingsClient` с
  возможностью переопределения. Согласовать с бэкендом до мержа.

### C. TokenStorage — безопасное хранение токенов (expect/actual)

Заменить `CryptoHelper` (XOR/iOS, детерминированный ключ/Desktop) на `expect`/`actual` `TokenStorage`.

```kotlin
// meetings-sdk/src/commonMain/.../auth/TokenStorage.kt
expect class TokenStorage(platformContext: Any? = null) {
    fun storeTokens(accessToken: String, refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun clearTokens()
}
```

**Android `Context`:** конструктор принимает `platformContext`, который в `androidMain` приводится
к `android.content.Context` для `EncryptedSharedPreferences`. На iOS/Desktop параметр игнорируется.
DI-модуль (Koin) передаёт `Context` через `androidContext()` на Android и `null` на остальных платформах.

| Платформа | Реализация | Защита |
|-----------|-----------|--------|
| **Android** | `EncryptedSharedPreferences` (уже работает) | AES-256 через AndroidKeyStore |
| **iOS** | Keychain Services (`SecItemAdd`/`SecItemCopyMatching`) | `kSecAttrAccessibleWhenUnlockedThisDeviceOnly` |
| **Desktop macOS** | `ProcessBuilder("security", "add-generic-password", ...)` | macOS Keychain |
| **Desktop Linux** | `libsecret` через JNA (Secret Service API) | Secret Service DBus |
| **Desktop Windows** | DPAPI через `CryptProtectData` (JNA) | Windows Data Protection API |

`SettingsTokenManager` переписывается: использует `TokenStorage` вместо `CryptoHelper`.
`CryptoHelper` удаляется полностью (становится избыточным при нативном хранении).

**Desktop-реализация:** поскольку `desktopMain` — единый JVM source set, разделение по ОС делается
через runtime-ветвление: `System.getProperty("os.name")` → выбор бэкенда (Keychain / libsecret / DPAPI).
Альтернатива (если JNA нежелателен): создать отдельные source sets `macosMain`/`linuxMain`/`windowsMain`
в convention-плагине `kmp.library.desktop` — но это отдельная задача, не в этом плане.
**На данном этапе:** runtime-ветвление с JNA/JNR для доступа к нативным API.

### D. Ktor-клиент — безопасность

**D.1 — sendWithoutRequest → host-scoped**

Заменить `sendWithoutRequest { true }` на:
```kotlin
sendWithoutRequest { request -> request.url.host == expectedApiHost }
```
`expectedApiHost` вычисляется в конструкторе `MeetingsClient` из `baseUrl`:
```kotlin
import io.ktor.http.Url
private val expectedApiHost = Url(baseUrl).host  // KMP-совместимый (не java.net.URL!)
```

**D.2 — HTTPS-валидация**

В `MeetingsClient` factory добавить:
```kotlin
require(baseUrl.startsWith("https://") || baseUrl.contains("localhost")) {
    "Production baseUrl must use HTTPS: $baseUrl"
}
```
Для debug-сборок проверка может отключаться через `BuildConfig.DEBUG`.

**D.3 — TLS / certificate pinning (на данном этапе — минимально)**

Добавить в OkHttp engine (Android/Desktop) `certificatePinner` с SHA-256 пинами production-сервера.
Пины вынести в конфигурацию (не хардкодить). Для iOS — `NSURLSession` delegate.
Если pinning откладывается — зафиксировать отдельной задачей с priority=high.

### E. mainDispatcher + DefaultDispatcherProvider для iOS

**E.1 — mainDispatcher actual**

Добавить в `meetings-sdk/src/iosMain/`:
```kotlin
actual fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main
```

**E.2 — DefaultDispatcherProvider actual**

Класс `DefaultDispatcherProvider` существует в `androidMain` и `desktopMain` как обычный класс
(не `expect`/`actual` — просто платформенный класс, реализующий `DispatcherProvider`).
Добавить в `meetings-sdk/src/iosMain/` **без ключевого слова `actual`** (в commonMain нет `expect class`):
```kotlin
class DefaultDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val io: CoroutineDispatcher = Dispatchers.Default // Kotlin/Native: нет отдельного IO
}
```

**E.3 — Desktop mainDispatcher fix (попутно)**

В `desktopMain` заменить `Dispatchers.Default` на `Dispatchers.Main`:
```kotlin
actual fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main
```
Убедиться, что `kotlinx-coroutines-swing` подключён в `desktopMain` dependencies.

### F. Попутные мелкие исправления

- **`AuthToken`:** заменить `@JvmInline value class AuthToken(...)` на `value class AuthToken(...)` —
  Kotlin 2.x поддерживает `value class` нативно на всех платформах, `@JvmInline` не нужен.
- **`Meeting` доменная модель:** `hasLocation` (см. секцию A).

---

## Порядок работ (вертикальный срез, от критического к важному)

### Шаг 1: Компиляционные + контрактные блокеры

| # | Задача | Модуль / source set |
|---|--------|---------------------|
| 1 | **mainDispatcher actual для iOS** | `meetings-sdk/src/iosMain/` |
| 2 | **DefaultDispatcherProvider actual для iOS** | `meetings-sdk/src/iosMain/` |
| 3 | **Desktop mainDispatcher → Dispatchers.Main** | `meetings-sdk/src/desktopMain/` |
| 4 | **MeetingDto: добавить `source`, `externalUrl`, `isOnline`** | `meetings-sdk/src/commonMain/` |
| 5 | **Meeting: добавить `source`, `externalUrl`, `isOnline`, `hasLocation`** | `meetings-sdk/src/commonMain/` |
| 6 | **Обновить `MeetingDtoMapper` + тесты (TIMEPAD-данные)** | `meetings-sdk/src/commonMain/` + `commonTest/` |

**Проверка:** `./gradlew :meetings-sdk:allTests` + `assembleDebug`

### Шаг 2: Аутентификация + хранение токенов

| # | Задача | Модуль / source set |
|---|--------|---------------------|
| 7 | **AuthResponse → accessToken + refreshToken** | `meetings-sdk/src/commonMain/` |
| 7a | **AuthToken: `value class(token: String)` → `data class(accessToken, refreshToken)`** | `meetings-sdk/src/commonMain/` |
| 8 | **RefreshTokenResponse DTO** | `meetings-sdk/src/commonMain/` |
| 9 | **RefreshTokenBody: access → refresh токен** (поле `token` → `refreshToken`) | `meetings-sdk/src/commonMain/` |
| 10 | **Bearer plugin: добавить `refreshTokens` блок** | `meetings-sdk/src/commonMain/` |
| 11 | **Bearer plugin: обработка неудачного refresh (clear + logout)** | `meetings-sdk/src/commonMain/` |
| 12 | **TokenStorage expect/actual (Android/iOS/Desktop)** | `meetings-sdk/src/{commonMain,androidMain,iosMain,desktopMain}/` |
| 13 | **TokenProvider: расширить методами `getAccessToken()`/`getRefreshToken()`/`saveTokens(AuthToken)`/`clearTokens()`; добавить `val isLoggedIn: StateFlow<Boolean>`** | `meetings-sdk/src/commonMain/` |
| 13a | **SettingsTokenManager → TokenStorage + два ключа** (`access_token` и `refresh_token`) | `meetings-sdk/src/commonMain/` |
| 14 | **AuthApi: обновить `refreshToken()`, endpoint-ы** | `meetings-sdk/src/commonMain/` |
| 15 | **DI-модули: заменить CryptoHelper на TokenStorage; передавать `Context` на Android** | `composeApp/src/commonMain/` |
| 16 | **Desktop TokenStorage fallback: при недоступности нативных API → encrypted-файл с OS-derived ключом** | `meetings-sdk/src/desktopMain/` |
| 16a | **Удалить CryptoHelper** | все source sets |

**Проверка:** `./gradlew :meetings-sdk:allTests` + `assembleDebug` + iOS/desktop сборка

### Шаг 3: Ktor-клиент — безопасность

| # | Задача | Модуль |
|---|--------|--------|
| 17 | **sendWithoutRequest → host-scoped** | `meetings-sdk/src/commonMain/` |
| 18 | **expectedApiHost из baseUrl** | `meetings-sdk/src/commonMain/` |
| 19 | **HTTPS-валидация baseUrl** | `meetings-sdk/src/commonMain/` |
| 20 | **TLS/pinning — минимально (OkHttp certificatePinner + конфиг)** | `meetings-sdk/src/androidMain/`, `desktopMain/` |

**Проверка:** `./gradlew assembleDebug`

### Шаг 4: Финальный гейт

| # | Задача |
|---|--------|
| 21 | `./gradlew :meetings-sdk:allTests` — commonTest на всех таргетах |
| 22 | `./gradlew assembleDebug` — Android сборка |
| 23 | iOS сборка (Xcode / Kotlin Native) |
| 24 | Desktop сборка |
| 25 | `./gradlew detekt` |

---

## Тестирование
- **Каждый шаг:** commonTest на изменённых модулях
- **После шага 1:** сборка android + desktop (iOS — после mainDispatcher fix)
- **После шага 2:** unit-тесты AuthApi refresh flow, TokenStorage per-platform,
  MeetingDtoMapper с TIMEPAD-фикстурами (пустые host/participants/capacity=0)
- **После шага 4:** Android device-smoke (SM-0 launch-only)

---

## Риски
- **iOS Keychain** — требует interop с Security framework через Kotlin/Native `platform.*`.
  Keychain-вызовы могут требовать main thread на iOS — обернуть в `withContext(Dispatchers.Main)` в `iosMain`.
  Митигировать: изолировать в `meetings-sdk/src/iosMain/`, тестировать на iOS device
  (не симуляторе — Keychain на симуляторе доступен не полностью).
- **Desktop TokenStorage** — runtime-ветвление по `os.name` для macOS/Linux/Windows.
  Митигировать: JNA dependency только в `desktopMain`, fallback на encrypted-файл с OS-derived ключом
  если нативные API недоступны.
- **Смена auth endpoint-ов** — требует координации с бэкендом.
  Митигировать: endpoint-префиксы конфигурируемы, по умолчанию — текущие пути.
  Переход на целевые `send-otp`/`verify-otp` — после подтверждения бэкенда.
- **Десериализация MeetingDto** — бэкенд может не слать `source`/`isOnline`.
  Митигировать: все поля с дефолтами, `kotlinx.serialization` игнорирует отсутствующие поля
  при дефолтах и `ignoreUnknownKeys = true`.
- **Certificate pinning** — отложено до отдельной задачи (Issue `certificate-pinning`).
  Без pinning MITM возможен при компрометации DNS/прокси. Приоритет: high, создать Issue до мержа текущего PR.
- **Атомарность записи токенов** — Android (EncryptedSharedPreferences) и iOS (Keychain) атомарны.
  Desktop (JNA): запись в один файл/транзакцию. При рассинхронизации — `clearTokens()` на 401 в B.4.

---

## Convention-плагины (НЕ трогаем)
Новые плагины (`meet.kmp.*`) **не создаются**. Используем **только существующие**:
`kmp.library.*` (base/android/desktop/ios/all) и `jetbrains-compose.*` — как требует AGENTS.md §5.

---

## Out of scope (явно)
- **Навигация → Decompose** — отдельная задача (отдельный Issue). Текущий `MeetNavController`
  функционален, миграция на Decompose не должна блокировать критические фиксы безопасности.
- **wasmJs / web-таргет** — задел на будущее, отдельная задача.
- **Yandex MapKit** — отдельная задача за expect/actual позже.
- **Coil 3 / multiplatform-paging / ktorfit+KSP для wasmJs** — решать в задаче «wasmJs target».
- **Дизайн-полировка** — цель: функциональный паритет и безопасность.
- **Бэкенд `meet-backend-v3`** — отдельный репозиторий.
- **Новые Gradle-модули** — весь код идёт в существующие `meetings-sdk`, `composeApp`,
  `core:uikit` согласно §5a. Разделение — на уровне пакетов, не модулей.
