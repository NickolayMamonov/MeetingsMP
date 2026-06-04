---
name: kmp-ci-cd
description: CI/CD pipeline for MeetingsMP — quality-checks workflow, detekt, local gate, and GitHub Actions configuration
license: MIT
compatibility: opencode
metadata:
  project: MeetingsMP
  ci: GitHub Actions
---

## CI Workflow (.github/workflows/quality-checks.yml)

### Trigger
- Pull requests to `main` and `develop`
- Pushes to `main` and `develop`
- Concurrency: cancel in-progress runs on same ref

### Jobs

| Job | Blocking | What it runs |
|-----|----------|-------------|
| build-and-test | YES | JDK 21 + Android SDK + desktopTest + testDebugUnitTest + assembleDebug |
| detekt | YES | ./gradlew detekt |
| android-lint | NO (continue-on-error) | ./gradlew lintDebug |

## Local Gate (before commit)

Run and achieve GREEN:
```bash
./gradlew detekt --no-daemon --console=plain
./gradlew desktopTest --no-daemon --console=plain
./gradlew testDebugUnitTest --no-daemon --console=plain
./gradlew assembleDebug --no-daemon --console=plain
```

## Detekt Configuration

- Config: `config/detekt/detekt.yml` and `config/detekt/detekt-compose.yml`
- Baseline: may exist per module (commit if updated)
- Convention plugin: `detekt` plugin available for all modules
- CI blocks on detekt errors, NOT on warnings

## Branch Protection

- `develop` is default branch
- PR required to merge
- Green CI (build-and-test + detekt) required
- CODEOWNERS approval required
- Force-push and branch deletion disabled
- Bypass: empty (human-only merge)

## Adding a New CI Check

1. Add step to quality-checks.yml
2. Mark as blocking (no continue-on-error) if it's a quality gate
3. Update AGENTS.md local gate section
4. Update this skill
5. Test on a non-critical PR first

## Prohibited
- Never bypass CI checks for time pressure
- Never modify CI config without a separate issue
- Never merge without green CI + CODEOWNERS approval
