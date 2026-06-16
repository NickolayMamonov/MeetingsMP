# AGENTS.md — правила работы агентов в MeetingsMP

Этот файл читают все агенты opencode (plan / build / review). Правила обязательны.
Пакет проекта: `dev.whysoezzy.meetings`. Сборка: Gradle + convention plugins (`gradle/conventions-plugins`).

## §N Контракт агрегатора и web-таргета (нерушимо)

### Агрегатор (клиент — потребитель внешних событий)
- Формат `MeetingDto` (`time: Long` + `date: String`) НЕ ломать. Новые поля — только аддитивно.
- Внешние (Timepad) события: user-generated поля (host/community/capacity/participants) могут быть
  пустыми. UI обязан корректно рендерить их отсутствие, не падать и не показывать заглушки-мусор.
- Карта/адрес показываются ТОЛЬКО при `hasLocation = !isOnline && lat != 0.0 && lng != 0.0`.
  Для онлайн-событий и событий без координат блок карты скрыт.
- Регистрация на событие = переход по `externalUrl` (внешняя страница), не внутренняя запись.
- `YANDEX_MAPKIT_API_KEY` только из `local.properties` через `buildConfigField`. Ключ не коммитится,
  `local.properties` в `.gitignore`.

### Web / wasmJs-таргет
- Любая зависимость в `commonMain` обязана поддерживать ВСЕ заявленные таргеты, включая `wasmJs`.
  Перед добавлением — сверить wasmJs-совместимость (koin, ktor/ktorfit+KSP, multiplatform-settings).
  KSP-процессоры особенно рискованны на wasmJs.
- Платформенное (Yandex MapKit на Android и т.п.) — за `expect`/`actual` или в leaf source set,
  НИКОГДА в `commonMain`. Web-версия карты — отдельная `actual`, не общий код.

## 1. Главный принцип

Доверяем не агенту, а **объективному гейту**. Любое утверждение «работает» должно быть
подтверждено машинно: зелёный CI, прошедший тест, golden-дифф, exit-код. «Выглядит правильно»
без проверки — не аргумент.

## 2. Изоляция (нерушимо)

- Одна задача = один GitHub Issue = одна ветка `feature/<issue>-<slug>` = один git worktree = одна сессия.
- Агенты **не делят** рабочее дерево и общаются только через git/PR. Никакого общего runtime-состояния.
- Не трогать чужие ветки и незакреплённые за задачей файлы.

## 3. Процесс задачи

1. **Plan** (агент `plan`): прочитать Issue, составить план по шагам, согласовать. Код не менять.
2. **Build** (агент `build`): реализовать строго по плану, шаг за шагом. На каждый шаг —
   структурный чеклист затронутых файлов перед правкой. Реализация полная, без TODO-заглушек.
3. Прогнать локальный гейт (см. §6) до коммита.
4. Открыть PR со ссылкой на Issue. Заполнить, что и как проверено (артефакты).
5. **Review** (агент `review`): кросс-модельное ревью, только чтение.
6. Мерж — **только человек** после зелёного CI и аппрува (CODEOWNERS).

## 4. Запрещено агентам

- **Self-merge**: агент никогда не мержит свой PR и не пушит в `main`/`develop` напрямую.
- `git push --force` / `-f` в любые ветки.
- Менять конфиг CI, branch protection, секреты без явной отдельной задачи.
- Трогать проект «Meeting» (Android-only) — он вне этого репозитория и не редактируется.

## 5. Архитектурные границы (нарушение = красный CI)

Модули и их роли:

- `meetings-shared`, `meetings-sdk` — бизнес-логика и данные. Максимум кода живёт в `commonMain`.
- `core:uikit` — общие UI-компоненты Compose (namespace `dev.whysoezzy.meetings.compose`).
- `composeApp` — общий слой приложения.
- `meetings-android`, `meetings-desktop`, `iosApp` (+ будущий web) — **тонкие** точки входа на платформу.

Правила:
- Бизнес-логику не класть в платформенные модули и не дублировать между ними.
- Новый общий код — в `commonMain` соответствующего модуля, а не в `androidMain`/`desktopMain`,
  если он не платформенно-специфичен.
- Не вводить нелегальные зависимости между фичами/слоями. Если сомневаешься — спроси в Plan-фазе.
- Подключение таргетов — только через существующие convention plugins
  (`kmp.library.*`, `jetbrains-compose.*`), не дублировать конфиг в build-файлах модулей.

### 5a. Миграция из Meeting Android — куда класть код

Весь код, переносимый из Meeting Android (https://github.com/NickolayMamonov/Meeting),
размещается в **meetings-sdk** (НЕ в meetings-shared).

| Тип кода | Куда (meetings-sdk) | Пакет |
|----------|---------------------|-------|
| Domain-модели | `meetings-sdk/src/commonMain/` | `dev.whysoezzy.meetings.domain.models` |
| Утилиты / ErrorType / CrashReporter | `meetings-sdk/src/commonMain/` | `dev.whysoezzy.meetings.common.*` |
| DTO / мапперы | `meetings-sdk/src/commonMain/` | `dev.whysoezzy.meetingssdk.models` |
| API-интерфейсы / Ktor-клиенты | `meetings-sdk/src/commonMain/` | `dev.whysoezzy.meetingssdk.api` |
| UseCases / Repository-интерфейсы | `meetings-sdk/src/commonMain/` | `dev.whysoezzy.meetings.domain.*` |
| ViewModel-ы | `composeApp/src/commonMain/` | `dev.whysoezzy.meetings.*.presentation` |
| Composable-экраны | `composeApp/src/commonMain/` | `dev.whysoezzy.meetings.*.presentation` |

**Почему не meetings-shared:** модуль meetings-shared содержит только iOS-таргет
(iosMain). У него нет desktop-таргета, поэтому `desktopTest` и `compileKotlinDesktop`
для него недоступны. meetings-sdk имеет все таргеты (android, desktop, ios).

**Платформенно-специфичный код (expect/actual):**
- `expect` — в `meetings-sdk/src/commonMain/`
- `actual` (Android) — в `meetings-sdk/src/androidMain/`
- `actual` (Desktop) — в `meetings-sdk/src/desktopMain/`
- `actual` (iOS) — в `meetings-sdk/src/iosMain/`

**Не редактировать meetings-shared/build.gradle.kts** — миграционный код туда не кладётся.

## 6. Локальный гейт перед коммитом

Запускать и добиваться зелёного:

```
./gradlew detekt
./gradlew :meetings-shared:allTests   # commonTest бизнес-логики
./gradlew :core:uikit:testDebugUnitTest
./gradlew assembleDebug                # сборка Android-таргета
```

Для UI-изменений добавить/обновить Roborazzi golden и приложить дифф к PR.
Не коммитить при красном detekt или падающих тестах.

## 7. Тестирование (по убыванию объективности)

1. `commonTest` — unit бизнес-логики (основной сигнал, дёшево, все таргеты).
2. Compose `runComposeUiTest` + Roborazzi golden — UI-поведение и визуал.
3. GMD (Gradle Managed Devices) — instrumented в CI.
4. MCP `mobile` (claude-in-mobile) — глаза/руки во внутренней петле: рендер, тап, logcat, assert.
   Это **вспомогательный** сигнal для отладки, **не** authoritative-гейт. Использует только агент `build`.

Для стабильности device-тестов: `wait_for_element` вместо пауз, отключённые анимации,
фиксированный locale, заранее засеянное состояние, одноразовый эмулятор.

## 8. Дизайн (опционально)

Только если Issue помечен `needs-design`: визуальная спека (прототип + скриншоты + design intent)
лежит в Issue / `docs/design/`. Критерий приёмки — собранный Compose-экран совпадает с прототипом
(сверка через Roborazzi golden). Перевод прототипа в Compose делает агент `build`.

## §9 Контракт агрегатора и web-таргета (нерушимо)

### Агрегатор (клиент — потребитель внешних событий)
- Поля `MeetingDto` не переименовывать/не удалять. Новые — только аддитивно, с дефолтами.
  Канон: id, imageUrl, title, description, time:Long, date:String,
  address{address, latitude, longitude}, capacity, tags[{id,text}], personHost?, communityHost?,
  participants[], meetingStatus, isUserInParticipants, source, externalUrl?, isOnline.
- Внешние события (source="TIMEPAD"): personHost/communityHost = null, participants пуст,
  capacity может быть 0. UI обязан рендерить это без падений и без заглушек-мусора.
- Карта/адрес показываются ТОЛЬКО при hasLocation = !isOnline && address.latitude != 0.0
  && address.longitude != 0.0. Иначе блок карты скрыт.
- Регистрация на внешнее событие = открыть externalUrl, НЕ внутренний join.
- Авторизация — OTP: /auth/send-otp → /auth/verify-otp, токены через /auth/refresh.
- YANDEX_MAPKIT_API_KEY только из local.properties, не в гит.

### Web / wasmJs-таргет
- Любая зависимость в `commonMain` обязана поддерживать ВСЕ заявленные таргеты, включая `wasmJs`.
  Перед добавлением — сверить wasmJs-совместимость (koin, ktor/ktorfit+KSP, multiplatform-settings).
  KSP-процессоры особенно рискованны на wasmJs.
- Платформенное (Yandex MapKit на Android и т.п.) — за `expect`/`actual` или в leaf source set,
  НИКОГДА в `commonMain`. Web-версия карты — отдельная `actual`, не общий код.

## 10. Модели и оркестрация

### Агенты и их модели

| Агент | Режим | Модель | Роль |
|-------|-------|--------|------|
| `orchestrator` | primary | GLM-5.1 | Автоматический цикл: координация plan → build → review → CI/CD → PR |
| `plan` | all | GLM-5.1 | Анализ Issue и проектирование решения (read-only) |
| `build` | all | DeepSeek V4 Flash | Реализация простых/средних задач |
| `developer-complex` | subagent | DeepSeek V4 Pro | Реализация сложных задач (архитектура, expect/actual, новые модули) |
| `review` | subagent | DeepSeek V4 Pro | Объективное ревью (другое семейство чем build/plan) |
| `ci-cd` | subagent | DeepSeek V4 Flash | Локальный гейт + создание PR |

`review` разведён с `build` по модели (Pro vs Flash) и с `plan` по семейству (DeepSeek vs GLM) —
это максимум кросс-модельности из двух семейств. Используем только GLM и DeepSeek (через Go).

### Автоматический цикл (orchestrator)

При запуске `/cycle` или выборе агента `orchestrator`:

1. **FETCH** — `gh issue list --label agent-task --state open` → выбрать Issue
2. **PLAN** — `@plan` проектирует решение (если плана ещё нет в Issue)
3. **BRANCH** — создать `feature/<issue>-<slug>`, изолировать через git worktree
4. **BUILD** — сложные задачи → `@developer-complex`; простые → `@build`
5. **REVIEW** — `@review` проверяет код; при замечаниях → возврат к build
6. **CI/CD** — `@ci-cd` прогоняет гейт и создаёт PR
7. **MERGE** — только человек (CODEOWNERS), не агент

### Команды

- `/cycle` — полный автоцикл: Issue → plan → code → review → CI/CD → PR
- `/next-issue` — взять Issue и спроектировать (без кода)
- `/implement` — реализовать текущую задачу (plan → code → гейт)
- `/review` — ревью текущих изменений или PR
- `/fix-review` — исправить замечания ревьюера
- `/deploy` — CI/CD: гейт → PR

### Стиль

- Коммиты: Conventional Commits, на русском или английском, в теле — `Closes #<issue>`.
- Комментарии в коде по необходимости; не оставлять закомментированный код и отладочный вывод.
