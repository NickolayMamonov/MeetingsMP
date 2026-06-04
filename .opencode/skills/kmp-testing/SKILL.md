---
name: kmp-testing
description: Testing patterns for MeetingsMP — commonTest, desktopTest, testDebugUnitTest, Roborazzi golden, and test hierarchy
license: MIT
compatibility: opencode
metadata:
  project: MeetingsMP
  platform: Kotlin Multiplatform
---

## Test Hierarchy (by objectivity, descending)

1. **commonTest** — unit tests for business logic (cheap, all targets)
2. **desktopTest** — JVM tests for common code (fast, no emulator)
3. **testDebugUnitTest** — Android unit tests (no emulator needed)
4. **Compose UI tests** — runComposeUiTest + Roborazzi golden
5. **Instrumented tests** — GMD (Gradle Managed Devices) in CI
6. **MCP mobile** — visual inspection (auxiliary, NOT authoritative)

## Running Tests

```bash
# Business logic (desktop = JVM target, fastest)
./gradlew :meetings-shared:allTests
./gradlew desktopTest

# Android unit tests
./gradlew testDebugUnitTest

# UI golden tests (if Roborazzi configured)
./gradlew recordRoborazziDebug   # record new goldens
./gradlew verifyRoborazziDebug   # verify against goldens
```

## Writing Tests

### commonTest Pattern
```kotlin
class MyViewModelTest {
    private val viewModel = MyViewModel(testRepository)

    @Test
    fun `initial state is loading`() = runTest {
        assertEquals(UiState.Loading, viewModel.state.first())
    }

    @Test
    fun `success state after data load`() = runTest {
        viewModel.loadData()
        assertIs<UiState.Content>(viewModel.state.drop(1).first())
    }

    @Test
    fun `error state on failure`() = runTest {
        repository.setShouldFail(true)
        viewModel.loadData()
        assertIs<UiState.Error>(viewModel.state.drop(1).first())
    }
}
```

### Test all states
- Loading → verify initial/loading indicator
- Content → verify data display
- Empty → verify empty state (e.g., "No items found")
- Error → verify error message and retry action

## Roborazzi Golden Tests

- Used for visual regression on Android
- Record: `./gradlew recordRoborazziDebug`
- Verify: `./gradlew verifyRoborazziDebug`
- Golden files stored in module's `src/test/snapshots/`
- Update golden when UI changes intentionally

## Prohibited
- No `assert(true)` or empty test bodies
- No tests that pass without assertions
- No skipping platform-specific tests (commonTest must pass everywhere)
- No hardcoded timeouts — use `runTest` with virtual time
