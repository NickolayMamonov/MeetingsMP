# MeetingsMP — Agentic Development System · Design Document

**Дата:** 2026-06-02
**Проект:** MeetingsMP (KMP: Android, iOS, Desktop; + планируемый Web/wasmJs)
**Статус:** Draft for Implementation
**Формат:** инвентаризация → систематизация → план → итеративные правки → саммари

---

## 0. Цель

Построить агентную систему разработки поверх MeetingsMP: проектирование и архитектура,
тестирование (автотесты + устройства/эмулятор), полноценный CI/CD и канбан. Агенты
работают **изолированно** друг от друга, а их результат оценивается **объективно** —
машинно-проверяемыми гейтами, а не доверием к агенту.

Принцип: *агенты пишут код в клетке, доверяем мы клетке.*

---

## 1. Инвентаризация (что уже есть в репозитории)

- Модули: `composeApp`, `meetings-android`, `meetings-desktop`, `meetings-shared`,
  `core:uikit`, `meetings-sdk`.
- Таргеты: Android, iOS, Desktop (JVM). Web пока нет.
- **Convention plugins уже есть** (`gradle/conventions-plugins` через `includeBuild`):
  `kmp.library.{all,android,base,desktop,ios}`, `jetbrains-compose.{all,android,base,desktop,ios}`,
  `android.{base,library}`, `jetpack-compose.base`. Это готовый фундамент «архитектура как ограничение».
- Включены typesafe project accessors.
- **Нет `.github`** — ноль CI и ноль гейта качества. Greenfield для клетки.

Источник логики: проект «Meeting» (Android-only) — **не трогаем**, только переносим из него
бизнес-логику в чистые common-модули MeetingsMP.

---

## 2. Систематизация (три плоскости)

### 2.1 Control plane — пульт
- OpenClaw: self-hosted gateway, управление с телефона (Telegram/Slack), изолированная сессия на агента.
- GitHub Projects — канбан и единый источник правды (issues = задачи).
- Безопасность OpenClaw обязательна: свежая версия (после CVE-2026-25253), токены, allowlist,
  `audit`, только за приватным периметром (за VPN), без публичного байндинга.

### 2.2 Execution plane — исполнители
- Изоляция: **1 задача = 1 Issue = 1 ветка = 1 git worktree / devcontainer = 1 сессия opencode**.
  Общее состояние только через git/PR, без общего runtime.
- opencode — кодинг-агент; `claude-in-mobile` подключён как MCP для внутренней петли
  (рендер и взаимодействие на Android/iOS/Desktop/Web через CDP).
- Разделение ролей: builder-агент и reviewer-агент — разные сессии. Self-merge запрещён.

### 2.3 Quality gate — объективная клетка
- Pre-commit: ktlint/detekt, компиляция, быстрые `commonTest`.
- CI: полный `commonTest`, Compose `runComposeUiTest`, Roborazzi, GMD, detekt, lint,
  secret/dependency-скан, CodeQL.
- Branch protection + CODEOWNERS + reviewer-агент. Прямой коммит и force-push в `develop`/`main` запрещены.

---

## 3. Жизненный цикл задачи

Дефолтный маршрут задачи: **Plan → Build → Gate → Review → Release**. Design — опциональная
фаза, подключается только по необходимости.

0. **Design (опционально, по запросу)** — только когда задача затрагивает новый/изменённый UI
   и нужна визуальная спека (удобно вешать лейбл `needs-design`). Claude Design: прототип →
   спека (HTML + скриншоты + design intent) в Issue / `docs/design/`. Большинство задач эту
   фазу пропускают.
   *Оговорки:* Claude Design выдаёт HTML/CSS/JS (не Compose), перевод прототип→Compose делает
   агент/человек; и он привязан к моделям Claude — это единственный Claude-зависимый кусок
   системы, поэтому держим его опциональным.
1. **Plan** — design doc + план по задачам (opencode plan mode). Заводится Issue, ветка, worktree.
2. **Build** — opencode пишет Compose; внутренняя петля через `claude-in-mobile`
   (рендер на нужном таргете, logcat, assert). Здесь же — порт логики из «Meeting».
3. **Quality gate** — открывается PR; прогон объективных проверок (см. §2.3 и матрицу §4).
4. **Review + merge** — reviewer-агент (свежий контекст) + твой аппрув (CODEOWNERS).
5. **Release** — Fastlane → Firebase App Distribution / Play internal track + деплой web.

Acceptance-критерий дизайна: собранный Compose-экран совпадает с утверждённым прототипом
(сверка через Roborazzi-golden и рендер `claude-in-mobile`).

---

## 4. Матрица таргетов и тестирования

| Таргет          | Сборка                       | Объективный тест                       | CI-раннер          |
|-----------------|------------------------------|----------------------------------------|--------------------|
| common (логика) | —                            | `commonTest` (unit)                    | linux (дёшево)     |
| Android         | `assembleDebug`              | Roborazzi + GMD instrumented           | linux              |
| Desktop (JVM)   | `:composeApp:run`            | `runComposeUiTest` + скриншоты         | linux              |
| iOS             | Xcode / Simulator            | `runComposeUiTest` + XCUITest/WDA      | **macOS (дорого)** |
| Web (wasmJs)    | `wasmJsBrowserDistribution`  | `wasmJsBrowserTest` (headless Chrome)  | linux + браузер    |

**Тиражирование гейта:** на каждый PR жёстко блокируем по `common + Android + Desktop`
(всё на linux, быстро). iOS и Web — required, но реже / на отдельном раннере, чтобы не платить
macOS-минутами за каждую мелочь.

**Эмулятор/устройства, по убыванию объективности:**
1. Roborazzi / Paparazzi — детерминированные golden-скриншоты (основной агентопригодный сигнал).
2. GMD (Gradle Managed Devices) — воспроизводимые headless instrumented-тесты в CI.
3. Firebase Test Lab / emulator.wtf — матрица реальных устройств на финал.
4. `claude-in-mobile` `assert_*` через CLI — вспомогательный сценарий, **не authoritative-гейт**.

---

## 5. Инструменты

| Слой              | Инструмент                              | Роль                                            |
|-------------------|------------------------------------------|-------------------------------------------------|
| Пульт             | OpenClaw                                 | управление агентами с телефона                  |
| Канбан / правда   | GitHub Projects + Issues                 | задачи, статусы, источник истины                |
| Дизайн            | Claude Design                            | прототип-спека, слайды, лендинг                 |
| Исполнитель       | opencode                                 | написание Compose-кода в изоляции               |
| Глаза/руки        | claude-in-mobile (MCP/CLI, CDP)          | рендер и взаимодействие на 4 таргетах           |
| Архитектура       | Gradle convention plugins                | границы модулей как машинное ограничение        |
| Тесты             | commonTest, runComposeUiTest, Roborazzi, GMD | объективный сигнал                          |
| Безопасность      | CodeQL, secret/dependency scan           | гейт безопасности                               |
| Релиз             | Fastlane + Firebase App Distribution / Play | дистрибуция                                  |

---

### 5.1 Модели и независимость от вендора

opencode — провайдер-агностик, но мы сознательно ограничиваемся тремя семействами: **GLM (Z.AI),
DeepSeek и Claude (Anthropic)**. Claude — одна из трёх опций, не монопольная зависимость.

Важно: `claude-in-mobile` и MCP, несмотря на «claude» в имени, model-agnostic (это сервер/CLI,
не модель), работают с любой моделью. В CI вместо `anthropics/claude-code-action` гоняем opencode
headless — так выбор модели остаётся за нами.

Роутинг по фазам — все три семейства задействованы, ревью намеренно на отдельном от build/plan
(имена моделей — на сегодня, финально через `opencode models`):

| Фаза / агент          | Модель (пример)                    | Канал / почему                                  |
|-----------------------|------------------------------------|-------------------------------------------------|
| Plan / архитектура    | `opencode/glm-5.1`                 | Go · сильное рассуждение, мало запросов          |
| Build / coder         | `opencode/deepseek-v4-flash`       | Go · много запросов, дёшево и быстро             |
| Build (сложный баг)   | `opencode/deepseek-v4-pro`         | Go · поднимаем модель точечно                    |
| Reviewer-агент        | `anthropic/claude-opus-4-7`        | BYOK · третье семейство, меньше слепых зон       |

Доступ под текущую подписку: **GLM и DeepSeek — через OpenCode Go** ($10/мес, flat-rate, оба
семейства входят), подключается командой `/connect` → OpenCode Go → Go-ключ; модели тогда
адресуются с префиксом `opencode/<id>` (подтвердить через `/models`). **Claude — через BYOK**
(собственный `ANTHROPIC_API_KEY`, pay-as-you-go), только на ревью. Дисциплина: не гнать всё через
одну модель (у Go 5-часовые лимиты на запросы — для роя параллельных build-агентов держать в уме);
flash — на скоростные агенты (build/test), сильный reasoning — на план и ревью. Модель назначается
на каждого агента отдельно в `opencode.json` (`agent.<name>.model`).

---

## 6. Изоляция и объективность (правила, которые нельзя нарушать)

- **Объективность:** любое «работает» подтверждается артефактом (зелёный CI, дельта покрытия,
  golden-дифф, прошедший assert).
- **Изоляция:** агенты общаются только через git/PR; общего runtime-состояния нет.
- **Архитектура как ограничение:** бизнес-логика только в `meetings-shared`/`meetings-sdk`
  (`commonMain`), UI — в `core:uikit` + общий `composeApp`, платформенные модули — тонкие точки входа.
  Нарушение границ = красный CI.
- **Независимость от вендора:** ядро на opencode с тремя семействами моделей (GLM, DeepSeek,
  Claude); ни одно не монопольно, роутинг по фазам — см. §5.1.

---

## 7. Web-таргет (отдельная фаза)

Статус: Compose Multiplatform for Web — **Beta**, таргет `wasmJs` (Kotlin/Wasm). Пригоден для
пред-продакшена; есть compatibility mode (Wasm + JS-fallback для старых браузеров).

Шаги:
1. Добавить по образцу существующих конвенций: `kmp.library.web.gradle.kts` и
   `jetbrains-compose.web.gradle.kts`; включить web в `*.all`-конвенции.
2. `wasmJs { browser() }` в `composeApp`, `meetings-shared`, `meetings-sdk`, `core:uikit`;
   точка входа `composeApp/src/wasmJsMain/kotlin/main.kt` (`ComposeViewport`) + `resources/index.html`;
   опционально тонкий модуль `meetings-web`.
3. Ktor: добавить `ktor-client-js` для wasmJs.
4. **Риск №1 — поддержка зависимостей в wasmJs.** До включения прогнать аудит каждой библиотеки
   (DI, локальная БД, settings, сериализация) на наличие wasmJs-артефакта.

Запуск/тест: `./gradlew wasmJsBrowserRun`, `wasmJsBrowserTest`, E2E — `claude-in-mobile` (CDP).

---

## 8. План внедрения (порядок = от гейта к рою)

- **Фаза 0 — Клетка.** `.github/workflows` (quality + `commonTest` + Android), pre-commit,
  branch protection, CODEOWNERS, ветки `develop`/`main`. До агентов.
- **Фаза 1 — Агент.** `AGENTS.md` + `opencode.json` (MCP `claude-in-mobile`), правила:
  design-first, task-by-task, PR без self-merge. Один агент в worktree делает PR по Issue.
- **Фаза 2 — Порт логики.** Перенос бизнес-логики из «Meeting» в `commonMain`, UIKit в `core:uikit`.
  Каждый кусок = Issue, проходит гейт.
- **Фаза 3 — Расширение гейта.** Добавить Desktop/iOS в матрицу, затем GMD.
- **Фаза 4 — Web.** Конвенции → wasmJs → аудит зависимостей → web в матрицу.
- **Фаза 5 — Дизайн-петля.** Claude Design как Design-фаза: прототип → спека в Issue →
  реализация в Compose → сверка рендера с прототипом.
- **Фаза 6 — Пульт и рой.** OpenClaw поверх; параллельные изолированные агенты.

---

## 9. Риски и ограничения

- Эмулятор/устройства в автономной петле — флак; обязательны `wait_for_element` вместо `sleep`,
  отключённые анимации, фиксированный locale, seeded state, одноразовый эмулятор.
- iOS требует macOS-раннера (стоимость); Web в Beta (зрелость, поддержка библиотек).
- OpenClaw молодой и мощный — изолировать, не давать публичный доступ.
- Claude Design: web-only research preview, нет прямого выхода в Compose, не звено CI;
  линковать подкаталог, а не весь монорепо.
- Стоимость: N агентов × эмуляторы × ферма устройств — закладывать роутинг моделей.

---

## 10. Открытые вопросы

- Devcontainer-на-задачу vs git worktree — какой уровень изоляции выбираем по умолчанию?
- JARVIS Mission Control v2 поверх OpenClaw или хватит GitHub Projects?
- Какой именно набор зависимостей переживает wasmJs (аудит до старта web-фазы)?
- Self-hosted runner с KVM vs облачная ферма для instrumented-тестов — по бюджету.

---

## 11. Саммари

Один контур: **Control plane (OpenClaw + GitHub Projects) → Execution plane (opencode + claude-in-mobile,
изоляция по worktree) → Quality gate (commonTest + Roborazzi + GMD + CodeQL + convention plugins)**,
с шестифазным циклом задачи **Design → Plan → Build → Gate → Review → Release** и матрицей по четырём
таргетам. Доверие — гейтам, не агентам. Начинаем с клетки, заканчиваем роем.
