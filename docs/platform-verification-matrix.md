# Матрица поддержки и проверки платформ

Матрица фиксирует утверждённые минимумы, требуемые будущие проверки и
фактические ограничения среды. Она **не является сертификатом совместимости**
Android, iOS, Desktop и Web. Общие технические границы задаёт
[AGENTS.md](../AGENTS.md); существующий локальный гейт для изменений кода там
сохраняется без изменений. Для документационных изменений product builds,
runtime и goldens имеют статус **N/A по scope**, а не PASS.

## Целевые платформы и минимальные примеры

| Target | Утверждённый минимум поддержки | Минимальные примеры runtime-проверки |
| --- | --- | --- |
| Android | Android 7.0 / API 24; текущие compile/target API 36 не меняются | Устройство или emulator API 24 и API 36 |
| iOS | iOS 18.2 | Simulator/device iOS 18.2 на совместимом Apple host |
| Desktop | Windows 11 x64, macOS 14 arm64, Ubuntu 24.04 x64 | Каждый указанный OS/architecture |
| Web `wasmJs` | Desktop Chrome 130+, Firefox 130+, Safari 18.2+ | Каждый указанный браузер на совместимом host после подключения target/tooling |

Это floors, а не перечень всех поддерживаемых конфигураций и не результаты
тестирования. Другие Desktop-варианты, Edge и mobile browsers вне гарантируемого
объёма этой матрицы, но не объявляются отключёнными. Для каждого фактического
запуска отдельно записывать версии runtime и окружения.

## Реальная конфигурация и границы модулей

Текущие общие source sets наследуются в Android/Desktop JVM и Apple/iOS
иерархиях. `meetings-sdk` владеет переносимыми domain/data и общими тестами;
`composeApp` — общей presentation-логикой и экранами; `core:uikit` — UI
компонентами; платформенные модули остаются тонкими оболочками.
`meetings-shared` агрегирует iOS framework и не заменяет тесты SDK.

`wasmJs` сейчас не настроен. Наличие Web в целевых требованиях не доказывает
наличие сборки, тестового target, browser runner или Web runtime pass.
Иерархия production source sets сама по себе не доказывает покрытие тестовых
source sets.

## Команды и покрытие Gradle

Команды ниже запускаются из корня репозитория. Они описывают обнаруженный
task graph/source coverage и ожидаемые assertions, но не утверждают, что
команды были выполнены успешно для продуктовой проверки в MEE6-3. Существующие
обязательные gates из `AGENTS.md` сохраняются.

| Команда | Обнаруженный охват и требуемое свидетельство |
| --- | --- |
| `./gradlew detekt` | В graph входят detekt tasks `composeApp`, `meetings-desktop`, `meetings-sdk`, `meetings-shared`, `core:uikit`. Требуются exit 0 и соответствующие отчёты; не включает Android-shell lint. |
| `./gradlew :meetings-shared:allTests` | Запускает aggregator simulator tests; в aggregator нет test source files, SDK tests не запускаются, iOS tasks отключены на Windows. Gate сохраняется, но не считается покрытием SDK. |
| `./gradlew :core:uikit:testDebugUnitTest` | Android/common test sources сейчас пусты; задача не доказывает UI test coverage. |
| `./gradlew assembleDebug` | Android outputs для `composeApp`, `meetings-android`, `meetings-sdk`, `core:uikit`; требуется реальная успешная сборка/APK. Это не iOS/Web runtime evidence. |
| `./gradlew desktopTest` | Включает `composeApp`, `meetings-desktop`, SDK и uikit tasks. SDK наследует пять `commonTest` файлов и три Desktop-файла; `composeApp` содержит два common-файла; uikit — один Desktop test file. Требуются фактические отчёты и число выполненных тестов, не `NO-SOURCE`. |
| `./gradlew testDebugUnitTest` | Включает `composeApp`, `meetings-android`, SDK и uikit. SDK/composeApp Android tests наследуют `commonTest`; пустые shell/uikit tasks не считаются выполненными тестами. |
| `./gradlew :meetings-sdk:desktopTest :meetings-sdk:testDebugUnitTest` | Прямые пути проверки общей SDK-логики. По отчётам подтвердить исполнение тестов из всех пяти SDK common test files. |
| `./gradlew :meetings-sdk:iosSimulatorArm64Test :composeApp:iosSimulatorArm64Test` | Каждая задача наследует свой `commonTest`; на Windows отключены. Нужны совместимый Apple simulator runner и фактические test reports. |
| `./gradlew :meetings-shared:linkDebugFrameworkIosArm64 :meetings-shared:linkDebugFrameworkIosSimulatorArm64` | Graph aggregator framework включает common consumers. Реальные артефакты требуют Apple capability; linking не доказывает launch/runtime. |
| `./gradlew :meetings-desktop:run` | Компилирует Desktop consumers и запускает приложение; требуются видимое работающее окно и отсутствие startup failure на каждом поддерживаемом host. |

SDK `commonTest` source files: `SafeApiCallTest`, `ApiExceptionTest`,
`CertificatePinsTest`, `ModelsSerializationTest`, `MeetingDtoMapperTest`.
Наличие файлов — только source coverage; доказательство выполнения — test
reports для точного SHA.

Web build/test/runtime tasks и Roborazzi golden commands сейчас недоступны и
здесь намеренно не выдумываются. Web target/tooling и настройка UI golden
требуют отдельно согласованной работы; альтернативный golden gate не утверждён.
Существующий CI охватывает JVM/Android, но не Apple/browser runtime. Xcode
project вызывает composeApp embedAndSign, а не aggregator; интеграция не
проверена и этой задачей не меняется.

## Текущие evidence rows

Каждая строка относится только к указанному source SHA и окружению. **PASS**
требует исполнения assertion; **FAIL** — опровергнутый assertion;
**BLOCKED** — отсутствует prerequisite, названы владелец и resume condition;
**UNRUN** — не исполнялось; **N/A** — проверка вне scope с объяснением.
Dry-run со статусом SKIPPED является только обнаружением графа, не runtime
evidence. При изменении SHA или среды требуется соответствующее новое
свидетельство.

| Проверка / target | SHA и runner | Assertion / результат | Evidence, владелец и условие продолжения |
| --- | --- | --- | --- |
| Документационная приемка MEE6-3 / все targets | Baseline `80bda089fea9724af0af3df3ac4e0dde665d43c5`; точный final SHA и runner фиксируются в implementation evidence manifest | Проверить только три согласованных документа, ссылки, whitespace и согласованность. Product checks **N/A**: изменены только документы. | Точные команды, outcomes и final SHA находятся в implementation evidence manifest; автор реализации. |
| Android product smoke | Baseline `80bda089fea9724af0af3df3ac4e0dde665d43c5`; Windows 11 x64/JDK 21.0.10 обнаружены, device/emulator не подтверждён | Launch-only smoke по [Android smoke-сценарию](smoke/android-smoke.md): foreground activity за bounded timeout и отсутствие fatal/ANR. **BLOCKED** для отсутствующего device runner; проверка не запускалась. | Владелец: Android runner/QA. Возобновить при доступном device/emulator; использовать и записать фиксированный locale `ru-RU`, app-data reset/signed-out fixture, image/API/orientation/device profile и network state. |
| Android/JVM Gradle checks | Baseline `80bda089fea9724af0af3df3ac4e0dde665d43c5`; Windows 11 x64/JDK 21.0.10 | Discovery graph найден, но product build/tests не запускались. **UNRUN**. Ранее обнаружены запрос Kotlin serialization compiler plugin 2.3.20-RC3 при объявленном Kotlin 2.3.0 и timeout загрузки; это ограничение baseline, не PASS и не доказанный дефект продукта. | Требуются реальные exact-SHA task outcomes и reports на подходящем Android/JVM runner. Владелец: build/CI owner; продолжить при разрешённом dependency resolution и выполнении gates. |
| iOS SDK/composeApp tests, framework и runtime | Baseline `80bda089fea9724af0af3df3ac4e0dde665d43c5`; Windows host без Xcode/iOS simulator evidence | Gradle Apple tasks отключены на Windows, runtime не выполнялся. **BLOCKED**. Xcode embedAndSign integration также не проверена. | Владелец: Apple runner/QA. Возобновить на совместимом macOS/Xcode host с iOS 18.2 simulator/device; сохранить test reports, фактические scheme/toolchain и launch assertion. |
| Desktop tests/runtime | Baseline `80bda089fea9724af0af3df3ac4e0dde665d43c5`; Windows 11 x64/JDK 21.0.10 | Task graph обнаружен; тесты и окно приложения фактически не запускались. **UNRUN**. Нужны тестовые отчёты и launch assertion на Windows 11 x64, macOS 14 arm64, Ubuntu 24.04 x64. | Владелец: Desktop runner/QA. Возобновить на каждом требуемом host; использовать и записать locale `ru-RU`, deterministic fixture/state, ОС/архитектуру и результат. |
| Web build/test/runtime | Baseline `80bda089fea9724af0af3df3ac4e0dde665d43c5`; browser runner не предоставлен | `wasmJs` target/tasks отсутствуют; runtime не запускался. **BLOCKED**. | Владелец: Web/build owner. Возобновить после отдельно утверждённой настройки target/tooling и browser runner; проверить реальный app surface и отсутствие startup/Wasm-load failure в Chrome 130+, Firefox 130+, Safari 18.2+. |
| Roborazzi golden для UI | Baseline `80bda089fea9724af0af3df3ac4e0dde665d43c5`; golden tooling/commands отсутствуют | Golden command недоступна; в MEE6-3 UI не менялся, поэтому применимость для этой задачи **N/A по scope**. Это не отменяет gate для будущих UI изменений. | Владелец: UI/build owner. Для будущего UI diff возобновить после настройки и отдельно согласованного гейта; не подменять golden отсутствием проверки. |

Окружение baseline: Windows 11 amd64 и JDK 21.0.10; Android SDK API 36 и
build-tools обнаружены при discovery. Наличие подключённого устройства,
browser automation и Desktop app launch не подтверждено. Переменные SDK и
локальные пути не являются частью tracked evidence.

## Runtime evidence protocol

Для применимых будущих product changes выполнить launch smoke на каждой
затронутой поддерживаемой платформе; изменения common logic требуют учитывать
всех потребителей. Функциональные assertions определяются только scope
конкретной задачи. Android smoke описан отдельно выше. Для iOS запускать
существующий Xcode project на выбранном iOS 18.2 runtime и фиксировать
фактические scheme, Xcode, SDK и способ запуска; для Desktop использовать
указанную `:meetings-desktop:run`; Web scenario возможен только после
подключения отсутствующего target и runner. Ни один launch smoke не доказывает
backend/feature parity.

Для каждого наблюдения использовать фиксированную test locale `ru-RU` и
контролируемый детерминированный fixture/state. Записывать полный source SHA,
команду/сценарий, source sets/consumers, locale, fixture ID/version или reset
procedure, device/OS/архитектуру, network state, версии tool/runtime,
assertion, exit/result и ссылку на доказательство. Ждать ожидаемое состояние
ограниченное время и сохранять sanitized диагностику/screenshots. Не включать
реальные credentials или персональные данные.
