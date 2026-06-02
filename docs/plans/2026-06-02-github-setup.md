# GitHub Setup — клетка для агентов (Фаза 0 / Этап 3)

Это настройки в UI GitHub, не файлы в репозитории. Цель — чтобы агент физически не мог
смержить в защищённые ветки мимо тебя. Делается один раз, до запуска автономных агентов.

Предполагается, что в репозитории уже лежат: `.github/workflows/quality-checks.yml` и `.github/CODEOWNERS`.

## 1. Ветки

1. Создай ветку `develop` от `main` (если ещё нет):
   ```
   git checkout -b develop main && git push -u origin develop
   ```
2. Сделай `develop` веткой по умолчанию для PR: Settings → General → Default branch → `develop`
   (рабочие PR агентов идут в `develop`, релизы — из `develop` в `main`).

## 2. Зарегистрировать статус-чек (важно сделать ДО защиты)

GitHub показывает чек в списке обязательных только после того, как он **хотя бы раз отработал на ветке**.
Наш workflow триггерится и на `push` в `main`/`develop`, и на `pull_request` — поэтому:

1. Влей `quality-checks.yml` в `develop` (через первый PR или прямым пушем до включения защиты).
2. Дождись, пока Actions прогонят job **`Build & Unit Tests (blocking)`** — теперь он появится в списке чеков.

## 3. Защита веток — вариант A: Rulesets (рекомендуется)

Settings → Rules → Rulesets → New branch ruleset. Создай два ruleset'а — для `main` и для `develop`
(или один с обоими таргетами; для `main` правила строже).

**Target branches:** добавь `develop` (и отдельным ruleset — `main`).
**Enforcement status:** Active.

Включи правила:
- ✅ **Require a pull request before merging**
  - Required approvals: **1**
  - ✅ Require review from Code Owners (привязка к `CODEOWNERS`)
  - ✅ Require approval of the most recent reviewable push (агент не аппрувит собственный последний пуш)
  - ✅ Dismiss stale approvals when new commits are pushed
- ✅ **Require status checks to pass**
  - ✅ Require branches to be up to date before merging
  - В поиске добавь чек: **`Build & Unit Tests (blocking)`**
  - (НЕ добавляй сюда `Static Analysis` — он пока non-blocking; добавишь после подключения detekt)
- ✅ **Block force pushes**
- ✅ **Restrict deletions**
- (для `main`) ✅ **Require linear history**

**Bypass list:** оставь пустым. Никаких ботов/агентов в обход — в этом весь смысл клетки.

## 4. Защита веток — вариант B: Classic (если привычнее)

Settings → Branches → Add branch protection rule. Branch name pattern: `develop` (затем отдельно `main`).
- ✅ Require a pull request before merging → Require approvals: 1 → Require review from Code Owners →
  Require approval of the most recent reviewable push
- ✅ Require status checks to pass before merging → Require branches to be up to date →
  выбрать `Build & Unit Tests (blocking)`
- ✅ Require conversation resolution before merging
- ✅ Do not allow bypassing the above settings
- (force-push и удаление в classic запрещены по умолчанию для защищённой ветки)

## 5. Прочие настройки репозитория

- Settings → General → Pull Requests: оставь только **Squash merge** (или squash+rebase), выключи
  обычный merge-commit при желании; включи **Automatically delete head branches**
  (worktree-ветки агентов будут чиститься сами).
- Settings → Actions → General → Workflow permissions: **Read repository contents** (минимум).
  Поднимать права только если конкретный workflow это требует.

## Ловушки (чтобы не потерять полдня)

- **Чек не виден в списке** → он ещё не прогонялся на ветке. Сделай push/PR, дождись Actions, обнови страницу.
- **Имена job'ов должны быть уникальны** между workflow — иначе required-чек становится неоднозначным и
  блокирует мерж. У нас job называется `Build & Unit Tests (blocking)` — держи его уникальным.
- **`Require approval of the most recent reviewable push`** — ключевой пункт против self-approve:
  без него агент, имеющий аппрув-права, мог бы протолкнуть свой же последний коммит.

## Критерий готовности Этапа 3

Прямой `git push` в `develop`/`main` отклоняется; PR нельзя смержить без зелёного
`Build & Unit Tests (blocking)` и аппрува CODEOWNERS. Если оба условия выполняются — клетка стоит.
