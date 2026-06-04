---
name: kmp-architecture
description: KMP architecture patterns for MeetingsMP — module boundaries, commonMain, expect/actual, convention plugins, and layer separation
license: MIT
compatibility: opencode
metadata:
  project: MeetingsMP
  platform: Kotlin Multiplatform
---

## Module Structure

```
meetings-sdk/       — Network/data layer (Ktor + Ktorfit). DO NOT add UI code here.
  commonMain/       — API interfaces, DTOs, auth, models
  androidMain/      — OkHttp engine
  desktopMain/      — OkHttp engine (JVM)
  iosMain/          — Darwin engine

meetings-shared/    — Business logic, ViewModels, UseCases, Repository interfaces
  commonMain/       — ALL business logic here
  iosMain/          — iOS XCFramework bridge only (thin)

core:uikit/         — Shared UI components, design tokens, theme
  commonMain/       — MeetingsTheme, Colors, Typography, Shapes, composable primitives

composeApp/         — Shared app layer (routing, DI setup)
  commonMain/       — App-level composables, navigation

meetings-android/   — Android entry point (thin)
meetings-desktop/   — Desktop entry point (thin)
meetings-shared/iosMain/  — iOS entry point (thin)
```

## Key Rules

1. **Business logic lives in commonMain** of meetings-shared or meetings-sdk — never in platform modules
2. **UI code lives in core:uikit** (primitives) or composeApp (screens)
3. **Platform modules are THIN** entry points only — MainActivity, main(), ViewController
4. **expect/actual**: declare `expect` in commonMain, implement `actual` per platform
5. **Convention plugins**: use kmp.library.* and jetbrains-compose.* plugins — do NOT duplicate config in build.gradle.kts
6. **Layers**: UI → ViewModel → UseCase → Repository → meetings-sdk API. Each layer only knows the one below
7. **Package naming**: `dev.whysoezzy.meetings` for app, `dev.whysoezzy.meetingssdk` for SDK (note double-s)

## Adding a New Module

1. Create module directory with build.gradle.kts
2. Add to settings.gradle.kts include
3. Apply existing convention plugin — DO NOT write raw KMP config
4. If a new convention plugin is needed, add it to gradle/conventions-plugins/
