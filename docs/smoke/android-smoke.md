# Android smoke — MeetingsMP (event aggregator, KMP + web target)

Device-conditional smoke the `ci-cd` agent runs **after** a green objective gate
(detekt + tests + assembleDebug), via the `mobile` MCP server (MOBILE_PROFILE=android).

- **Package:** `dev.whysoezzy.meetings`
- **Debug APK:** `meetings-android/build/outputs/apk/debug/meetings-android-debug.apk`
- **Device policy:** device/emulator present → SM-0 blocking; none → `smoke: SKIPPED (no device)`, not a fail.

> **State (current):** the UI migration to the aggregator screens landed incorrectly — screen
> content is not yet trustworthy. So the gate smoke is **launch-only**: it does NOT assert on any
> screen text. It verifies the app installs and starts without crashing. Screen-level assertions
> are deferred to the SA-* target suite below, filled in once the migration is correct.

---

## Gate smoke (blocking when a device is present)

### SM-0 — App installs and launches without crashing  (text-agnostic)
1. `install_app` → the debug APK path above
2. `launch_app dev.whysoezzy.meetings`
3. `wait` 4000 ms (let the first frame settle)
4. `get_current_activity` → assert the foreground activity belongs to `dev.whysoezzy.meetings`
   (the app is in front, i.e. it did not crash out or close)
5. `get_logs` filtered by `dev.whysoezzy.meetings` → assert **no** `FATAL EXCEPTION` /
   `AndroidRuntime` crash / ANR in the launch window
6. `screenshot` → attach to the gate report for manual eyeballing (no assertion)
- **Pass:** app foregrounded + no fatal log → install/launch path is healthy.
- **Fail:** activity not foreground OR a FATAL in logs → status FAIL, attach screenshot + logs.

That is the entire blocking smoke while screens are unstable. It deliberately avoids
`assert_visible` so a broken/renamed screen can't false-fail a PR.

---

## Target suite (NOT active — fill after the aggregator UI migration is correct)

When the migration lands, replace SM-0's screenshot step with real assertions on the aggregator
screens. Expected screens from the product logic (confirm exact strings on the migrated UI):

### SA-1 — Event feed renders  (needs backend or cached data)
- Feed sections: nearest-event banner, «Ближайшие встречи», «Сообщества», «Все встречи».
- `wait_for_element` a stable feed header → `assert_visible` it.
- `assert_not_exists "<real error-state string>"`.

### SA-2 — Map gating for online events  (logic check)
- For an **online** event (`isOnline=true`) or one without coords: map/address block is HIDDEN
  (`hasLocation = !isOnline && lat!=0 && lng!=0`). Assert the map block is absent.
- For an **offline** event with coords: map block is visible.

### SA-3 — External registration  (deep behavior, optional)
- Tapping «Зарегистрироваться» opens the external page (`externalUrl`) — verify an intent/browser
  hand-off, not an internal registration screen.

---

## Notes
- Non-UI tasks (refactor, build, ingestion-contract changes) → mark the PR `smoke: N/A`.
- Pull-to-refresh, Yandex MapKit, tag/city/date filters are aggregator features — add SA-* blocks
  for them only after the screens stabilize and you have real, stable element strings.
