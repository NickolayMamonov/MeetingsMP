---
name: code-review
description: Code review checklist and patterns for MeetingsMP — KMP-specific gotchas, architecture violations, and common review findings
license: MIT
compatibility: opencode
metadata:
  project: MeetingsMP
  process: review
---

## Review Checklist (ordered by priority)

### 1. KMP Architecture Boundaries (CRITICAL)
- [ ] Business logic is in `commonMain`, NOT in `androidMain`/`desktopMain`/`iosMain`
- [ ] Platform modules are THIN (just entry points)
- [ ] No Android-only APIs in commonMain (Hilt, android.*, LiveData, Bundle)
- [ ] expect/actual decomposition is correct (one expect → one actual per platform)
- [ ] No duplicate logic between actual implementations

### 2. Module Boundaries (CRITICAL)
- [ ] UI code doesn't import Ktor/Ktorfit
- [ ] Domain code doesn't import Compose/platform SDKs
- [ ] meetings-sdk contains only network/data layer code
- [ ] core:uikit is for design tokens + shared composable primitives
- [ ] No circular dependencies between modules
- [ ] Convention plugins used (no raw KMP config in build.gradle.kts)

### 3. Tests (CRITICAL)
- [ ] commonTest covers the change (not empty, not assert(true))
- [ ] Tests cover ALL states: loading, content, empty, error
- [ ] desktopTest and testDebugUnitTest pass
- [ ] For UI changes: Roborazzi golden updated

### 4. Code Quality (MAJOR)
- [ ] No TODOs, "fix later", empty bodies
- [ ] No commented-out code or debug output
- [ ] No hardcoded strings/colors/dimensions/URLs
- [ ] No hardcoded BASE_URL (especially localhost)
- [ ] Idiomatic Kotlin: null-safety, sealed classes, data classes
- [ ] previews (@Preview) for key composable states
- [ ] lifecycle-aware: viewModelScope, collectAsStateWithLifecycle

### 5. State Management (MAJOR)
- [ ] UI State: data class or sealed interface (not mutable vars)
- [ ] UI Events: sealed interface
- [ ] One-time effects: Channel/SharedFlow (not shared state)
- [ ] All four states handled: loading, content, empty, error
- [ ] No GlobalScope usage

### 6. Security (MAJOR)
- [ ] No secrets/tokens/keys in code
- [ ] No debug credentials
- [ ] Input validation present
- [ ] Error messages don't leak internals

### 7. Compose Performance (MINOR)
- [ ] @Immutable/@Stable annotations where beneficial
- [ ] No heavy work in composable functions
- [ ] Proper key usage in LazyColumn/forEach
- [ ] No unnecessary recompositions visible

## Common KMP Gotchas

1. **expect class in commonMain, actual class in platform** — both must have same visibility
2. **kotlinx.serialization** — annotate expect classes with @Serializable, not actual
3. **ViewModel** — use `org.jetbrains.androidx.lifecycle.ViewModel`, NOT `androidx.lifecycle.ViewModel`
4. **Compose resources** — use `composeApp/src/commonMain/composeResources/`, not Android `res/`
5. **Ktorfit** — KSP runs in commonMain, generates platform-agnostic code

## Verdict Format

```
## Summary
Brief overview

## Issues
### Critical (must fix)
- [ ] Issue with file:line — why it's critical + fix suggestion

### Major (should fix)
- [ ] Issue with file:line — why + fix suggestion

### Minor (nice to fix)
- [ ] Issue with file:line

## Verdict
APPROVE / REQUEST_CHANGES / APPROVE_WITH_SUGGESTIONS
```
