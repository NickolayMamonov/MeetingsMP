---
name: kmp-web
description: Web/KJS (wasmJs) patterns for MeetingsMP — preparation for future web target
license: MIT
compatibility: opencode
metadata:
  project: MeetingsMP
  platform: Web (wasmJs)
  status: planned
---

## Web Target Status

The web target (wasmJs) is **planned but not yet active**. When implementing, follow these guidelines.

## Pre-flight Checklist Before Activation

1. **Dependency audit**: Check all dependencies for wasmJs compatibility
   - Ktor client: CIO engine for web
   - kotlinx-serialization: compatible
   - kotlinx-coroutines: compatible
   - Compose Multiplatform: compatible for web

2. **Convention plugin**: Create `kmp.library.web` and `jetbrains-compose.web` in gradle/conventions-plugins/

3. **Ktor engine**: Web target uses CIO engine, not OkHttp

4. **File access**: No java.io.File on web — use platform-specific file APIs

## Web Module Patterns

### wasmJs Source Set
```kotlin
// build.gradle.kts for web-enabled module
plugins {
    id("kmp.library.base")  // or kmp.library.web when created
}

kotlin {
    sourceSets {
        val wasmJsMain by getting {
            dependencies {
                // web-specific dependencies
            }
        }
        val wasmJsTest by getting
    }
}
```

### Network on Web
- Ktor CIO engine for wasmJs
- No OkHttp (JVM-only)
- Same Ktorfit API — engine is pluggable

## Compose on Web
- Compose Multiplatform 1.10.0 supports wasmJs target
- Material3 should be compatible
- Design tokens from core:uikit should work
- Test rendering via browser, not emulator

## Prohibited
- JVM-specific APIs (java.io, java.net) in commonMain when targeting web
- Platform-specific Ktor engines in shared code — use expect/actual
- Blocking I/O on web — use suspend functions
