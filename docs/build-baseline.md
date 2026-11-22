# Reproducible build baseline

This document records a build/test baseline, not a platform compatibility
certification. It preserves the project's required checks and distinguishes
real test execution from tasks that are present in the Gradle graph but have
no test sources on this host.

## Measured revision and environment

Baseline commit: `0628bfed157855d9c407566b1766a22949e81db6` (branch
`MEE6-8`), measured on 2026-11-22 from a clean tracked and untracked
worktree. The successful rerun window was 16:36–16:46 UTC. The worktree was
Windows 11 x64 with Temurin JDK 21.0.10+7 and Gradle Wrapper 8.14.3. The
project pins Kotlin/KSP 2.3.0, Android Gradle Plugin 8.11.2, Compose
Multiplatform 1.10.0, Ktor 3.4.1, Ktorfit 2.7.3, and Koin 4.0.2. Android
compile/target SDK is 36; Android API 36 and build-tools 36.1.0 were available.

An installed Android SDK is required for Android-backed tasks. Set
`ANDROID_HOME` in the shell to that SDK root before running Gradle. Do not
commit local SDK configuration or credentials.

## Reproduction

First inspect the graph and prove task availability:

```text
./gradlew tasks --all --console=plain
```

The project's four mandatory commands remain:

```text
./gradlew detekt
./gradlew :meetings-shared:allTests
./gradlew :core:uikit:testDebugUnitTest
./gradlew assembleDebug
```

For forced task execution (without deleting shared caches), append
`--console=plain --stacktrace --rerun-tasks --no-build-cache` to each command.
For the SDK common tests and their consumers, also run:

```text
./gradlew desktopTest --console=plain --stacktrace --rerun-tasks --no-build-cache
./gradlew testDebugUnitTest --console=plain --stacktrace --rerun-tasks --no-build-cache
./gradlew :meetings-sdk:desktopTest :meetings-sdk:testDebugUnitTest --console=plain --stacktrace --rerun-tasks --no-build-cache
```

Each command must be run separately and its exit recorded. A dry run or an
`UP-TO-DATE` task is not execution evidence. Test XML is under each module's
`build/test-results/<task>/`; detekt reports are under
`<module>/build/reports/detekt/`.

## Results at the measured baseline

All seven actual invocations completed with exit code 0 on the successful
SDK-configured rerun:

| Command | Outcome and interpretation |
| --- | --- |
| `./gradlew detekt` | Passed; all five configured module reports were generated with zero findings. |
| `./gradlew :meetings-shared:allTests` | Exit 0, but the aggregate task was `NO-SOURCE`; iOS simulator test tasks were skipped on Windows. This is graph/availability evidence only, not a claim of test execution. |
| `./gradlew :core:uikit:testDebugUnitTest` | Exit 0, but the task was `NO-SOURCE`. This is graph/availability evidence only. |
| `./gradlew assembleDebug` | Passed and produced `meetings-android-debug.apk`; SHA-256 `90eb240720aaa0aba2401d62c154aec39f817f7c6df7d8371fbad0b41001b696`. |
| `./gradlew desktopTest` | Passed. Fresh SDK Desktop XML reports include all five required common test classes. |
| `./gradlew testDebugUnitTest` | Passed. Fresh SDK Android XML reports include all five required common test classes. |
| `./gradlew :meetings-sdk:desktopTest :meetings-sdk:testDebugUnitTest` | Passed; both SDK targets executed. |

The no-source interpretation for the shared aggregator and UIKit task was
explicitly approved for this baseline exercise. It does not waive or change
future product-test gates.

### SDK common test execution

Fresh XML reports from the SDK Desktop and Android test tasks were timestamped
within the successful run window. Each listed suite had zero failures, errors,
or skipped tests:

| `commonTest` class | Desktop | Android |
| --- | ---: | ---: |
| `SafeApiCallTest` | 6 | 6 |
| `ApiExceptionTest` | 8 | 8 |
| `CertificatePinsTest` | 7 | 7 |
| `ModelsSerializationTest` | 10 | 10 |
| `MeetingDtoMapperTest` | 12 | 12 |
| **Executed tests** | **43** | **43** |

This is JVM/Android test evidence only. It does not certify device runtime
behavior.

## Platform and infrastructure limits

- Android and Desktop tasks are configured and were executed as shown above.
- iOS targets are configured, but their simulator test tasks were skipped on
  this Windows host. There is no Xcode, simulator, or iOS runtime evidence in
  this baseline.
- The requested product target `wasmJs` is not configured. There is no Web
  build, browser test, or Web runtime evidence.
- No download timeout was reproduced in the recorded runs. Gradle and
  dependency caches were available; caches were not cleared and no cold-cache
  download or general network-health claim was tested.
- No current GitHub Actions outage was established or claimed. CI state must
  be checked for the exact PR head during review; these local results do not
  imply that remote CI passed.
- The successful measurement is tied to the baseline SHA above. A later PR
  head requires its own exact-head verification; do not relabel these results.
