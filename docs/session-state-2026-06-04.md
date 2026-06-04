# Agent System — Session State (2026-06-04)

## TL;DR
Собрана агентская система для KMP-проекта MeetingsMP:
- **OpenCode**: 6 агентов, 6 skills, 3 команды, правила, плагин
- **OpenClaw**: Gateway + Discord бот онлайн, отвечает на `!status`
- **Модели**: OpenCode Go (GLM-5.1, DeepSeek V4 Pro/Flash)

## Текущее состояние

### Gateway
- OpenClaw Gateway: Windows Scheduled Task, порт 18789, loopback
- Discord бот: `@MeetingsMP Bot`, онлайн, канал `meetings-mp`
- Dashboard: http://127.0.0.1:18789/ (auth=none)
- Команды: `!status` работает, `!cycle` / `!review` / `!deploy` настроены

### OpenCode
- `opencode run` работает из `C:\Users\whysoezzy\AndroidStudioProjects\Meetings`
- Default agent: orchestrator (GLM-5.1)
- `/cycle` / `/next-issue` / `/review` — команды готовы

### API ключи
- OpenCode Go: `env.OPENCODE_API_KEY` в `~/.openclaw/openclaw.json`
- Discord: токен в том же конфиге (не через env — вручную)
- Telegram: не настроен (отключён)

## Агенты OpenCode

| Агент | Режим | Модель | Роль |
|-------|-------|--------|------|
| orchestrator | primary | opencode-go/glm-5.1 | Автоцикл Issue→PR |
| plan | all | opencode-go/glm-5.1 | Проектирование |
| build | all | opencode-go/deepseek-v4-flash | Простые/средние задачи |
| developer-complex | subagent | opencode-go/deepseek-v4-pro | Сложные задачи |
| review | subagent | opencode-go/deepseek-v4-pro | Code review |
| ci-cd | subagent | opencode-go/deepseek-v4-flash | Гейт + PR |

## Файлы проекта MeetingsMP

Создано/изменено (относительно develop):

Изменено:
  opencode.json — 6 агентов + 6 команд
  AGENTS.md — добавлен §9 Оркестрация
  .github/workflows/quality-checks.yml — PR-comment job

Создано:
  .opencode/prompts/orchestrator.md
  .opencode/prompts/developer-complex.md
  .opencode/prompts/ci-cd.md
  .opencode/commands/cycle.md
  .opencode/commands/next-issue.md
  .opencode/commands/review.md
  .opencode/rules/kmp-patterns.md
  .opencode/plugins/notify-bridge.ts
  .opencode/skills/kmp-architecture/SKILL.md
  .opencode/skills/kmp-android/SKILL.md
  .opencode/skills/kmp-web/SKILL.md
  .opencode/skills/kmp-testing/SKILL.md
  .opencode/skills/kmp-ci-cd/SKILL.md
  .opencode/skills/code-review/SKILL.md
  scripts/start-openclaw.ps1

Файлы НЕ закоммичены — висят как unstaged changes.

## Файлы OpenClaw

  ~/.openclaw/openclaw.json — gateway + Discord + модели
  ~/.openclaw/workspace/AGENTS.md — инструкции бота
  ~/.openclaw/workspace/skills/opencode-bridge/SKILL.md
  ~/.openclaw/workspace/skills/kmp-tools/SKILL.md

## Что осталось

### Высокий приоритет
- [ ] Протестировать `!cycle` — нужен открытый agent-task issue в MeetingsMP
- [ ] Закоммитить изменения конфигурации в репо

### Средний приоритет  
- [ ] Установить `gh` CLI: `winget install GitHub.cli`
- [ ] После установки gh: `gh auth login` + обновить `!status` для показа issues

### Низкий приоритет
- [ ] Настроить Telegram канал (сейчас `enabled: false`)
- [ ] Включить `coding-agent` skill в OpenClaw
- [ ] Настроить `notify-bridge.ts` (env: DISCORD_WEBHOOK_URL и т.д.)

## Баги / Особенности

1. Discord перехватывает `/` как slash-команды → префикс `!` (!status, !cycle)
2. OpenClaw 2026.6.1: `channels.discord...allow` устарело → убрано
3. `identity.role` не поддерживается в 2026.6.1 → убрано
4. `channels.webchat` retired → удалён из конфига
5. Gateway auth: `mode: "none"` (локально, без пароля)
6. `opencode run` работает CLI, но медленно стартует

## Быстрый старт в следующей сессии

```powershell
# 1. Проверить что gateway жив
Invoke-WebRequest http://127.0.0.1:18789

# 2. Если нет — запустить
schtasks /Run /TN "OpenClaw Gateway"

# 3. Проверить бота в Discord: написать !status

# 4. Тест автоцикла:
cd C:\Users\whysoezzy\AndroidStudioProjects\Meetings
opencode run /cycle

# 5. Или через Discord:
!cycle
```
