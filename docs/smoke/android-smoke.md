# Android smoke — MeetingsMP

Этот сценарий — launch-only baseline для Android. Матрица применимости,
поддерживаемые версии и общие правила фиксации результата описаны в
[матрице платформ](../platform-verification-matrix.md).

- **Package:** `dev.whysoezzy.meetings`
- **Debug APK:** `meetings-android/build/outputs/apk/debug/meetings-android-debug.apk`
- **Runner:** для применимого product smoke нужен Android device/emulator. Если
  runner отсутствует, обязательная проверка имеет статус **BLOCKED**.
- **Документационные изменения:** product smoke имеет статус **N/A** с причиной
  «изменены только документы»; это не подтверждает работоспособность приложения.

> **Scope:** smoke остаётся только проверкой установки и запуска и не утверждает,
> что продуктовые экраны или функции работают. Экранные assertions в SA-* ниже
> неактивны и не являются частью этого baseline.

---

## Launch smoke (обязателен для применимых Android-изменений)

### SM-0 — приложение устанавливается и запускается без аварии
1. `install_app` → the debug APK path above
2. `launch_app dev.whysoezzy.meetings`
3. Установить фиксированную test locale `ru-RU`. Перед запуском очистить app
   data и использовать signed-out состояние без сохранённых credentials.
   Зафиксировать API, image/device profile, portrait orientation и выбранный
   network state; сохранять их неизменными между повторами. Если проверке нужны
   данные, использовать один и тот же версионированный sanitized fixture без
   персональных данных.
4. Ожидать ожидаемую foreground activity опросом состояния с ограничением 30 секунд.
   Фиксированная пауза не считается проверкой готовности.
5. `get_current_activity` → подтвердить, что foreground activity принадлежит
   `dev.whysoezzy.meetings`.
6. `get_logs` для окна запуска → подтвердить отсутствие `FATAL EXCEPTION`,
   AndroidRuntime crash и ANR.
7. Снять screenshot после готовности activity и приложить его вместе с
   sanitized логами и параметрами среды.

- **PASS:** activity приложения стала foreground в пределах timeout и в логах
  запуска нет fatal/ANR.
- **FAIL:** неверная activity, fatal/ANR либо timeout; приложить диагностические
  материалы.
- **BLOCKED:** обязательный device/emulator или необходимый доступный runner
  отсутствует; указать владельца runner и условие возобновления.
- **N/A:** только если task scope не затрагивает продукт, например docs-only;
  указать основание. Не использовать N/A для недоступного runner.

Результат привязывается к полному SHA проверенной ревизии и включает команду/
сценарий, source sets и consumers, ОС/архитектуру/API, locale `ru-RU`, fixture
или reset-процедуру, device profile, network state, assertion, результат и
ссылку на доказательства. Запуск приложения не подтверждает feature/API parity.

---

## Target suite (неактивен)

SA-* assertions не являются частью launch-only baseline и не должны объявляться
пройденными без отдельного согласованного изменения и реальных стабильных
assertions:

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
- Для затронутых Android product changes отсутствие runner означает **BLOCKED**,
  не `SKIPPED`; возобновить smoke, когда доступен device/emulator runner.
- Только документационная задача получает **N/A** для product smoke по scope.
- Pull-to-refresh, Yandex MapKit, tag/city/date filters не проверяются этим
  launch-only сценарием.
