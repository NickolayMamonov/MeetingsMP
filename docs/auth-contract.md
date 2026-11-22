# Authentication and upgrade contract

This document records the approved email-only product direction and the
backend contract selected for future client implementation. It distinguishes
immutable backend source evidence from deployed behavior and from the current
MeetingsMP client. It does not implement or certify an authentication change.

## Authority and evidence boundary

The selected backend authority is release **v1.7.0**, immutable commit
[`9362e47e64c402d7eb38fd2c7e0420e7b01e78a0`](https://github.com/NickolayMamonov/meet-backend-v3/tree/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0).
Relevant source includes its
[authentication controller](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/kotlin/dev/whysoezzy/meet/api/controller/AuthController.kt),
[DTOs](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/kotlin/dev/whysoezzy/meet/api/dto/AuthDto.kt),
[service](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/kotlin/dev/whysoezzy/meet/service/AuthService.kt),
[email normalization](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/kotlin/dev/whysoezzy/meet/service/auth/identifier/EmailAddressNormalizer.kt),
[request validation](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/kotlin/dev/whysoezzy/meet/service/auth/identifier/EmailOtpRequestValidator.kt),
[device-ID validation](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/kotlin/dev/whysoezzy/meet/service/auth/identifier/DeviceId.kt),
[error definitions](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/kotlin/dev/whysoezzy/meet/api/error/ApiError.kt),
[error handling](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/kotlin/dev/whysoezzy/meet/api/error/ApiExceptionHandler.kt),
[verification executor](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/kotlin/dev/whysoezzy/meet/service/auth/otp/OtpVerificationExecutor.kt),
[V6 schema migration](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/resources/db/migration/V6__email_otp_authentication.sql),
[release configuration](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/src/main/resources/application.yml), and the
[email-OTP operations guide](https://github.com/NickolayMamonov/meet-backend-v3/blob/9362e47e64c402d7eb38fd2c7e0420e7b01e78a0/docs/operations/email-otp.md).

These links establish source-level behavior only. The backend deployment
revision, live service responses, SMTP/provider readiness, effective runtime
configuration, and compatibility of a deployed client/server pair were not
independently verified. A response accepting a send request does not prove
delivery to an inbox.

## Backend API contract

| Endpoint | Request | Source-defined result |
| --- | --- | --- |
| `POST /auth/email/send-otp` | JSON `{ "email": "…" }`; optional `X-Device-Id` header | HTTP 202 with a generic message indicating that a code will be sent if the address can receive email. This avoids disclosing whether an account exists. Acceptance is not delivery confirmation. |
| `POST /auth/email/verify-otp` | JSON `email`, six ASCII digits in `code`; optional `name` and `surname` (each at most 100 characters); optional `X-Device-Id` | Authentication response with `accessToken`, `refreshToken`, `isNewUser`, and `user`. An email-authenticated profile may have a null phone number. |
| `POST /auth/refresh` | JSON `{ "refreshToken": "…" }` | Both replacement values: `accessToken` and `refreshToken`. The refresh token rotates. |
| `POST /auth/logout` | Authenticated bearer request | Invalidates the user's refresh tokens and increments the user's auth version; this is account-wide, not a per-device-only logout. |

If supplied, `X-Device-Id` must contain 16–128 safe ASCII characters from
`[A-Za-z0-9._~-]`. The documentation task does not authorize adding device
fingerprinting or making this header a product identity.

The backend canonicalizes email by trimming whitespace, applying Unicode NFC,
validating an ASCII local part, lowercasing that local part, converting the
domain to IDN ASCII and lowercasing it, then enforcing a 254-character
canonical maximum. Backend validation remains authoritative; this description
does not prescribe a platform-specific common-code normalization API.

The structured error envelope contains `status`, `message`, `timestamp`,
`path`, and `code`. Relevant source-defined errors include:

| HTTP | Code | Meaning |
| --- | --- | --- |
| 400 | `BAD_REQUEST` | Invalid/missing email or malformed request. |
| 401 | `OTP_INVALID_OR_EXPIRED` | Invalid, expired, or exhausted OTP verification. |
| 401 | `UNAUTHORIZED` | Rejected refresh token/session. |
| 429 | `OTP_RATE_LIMITED` | Email OTP send limit reached. |
| 503 | `OTP_DELIVERY_UNAVAILABLE` | Email delivery is temporarily unavailable. |
| 503 | `OTP_ACTIVATION_UNAVAILABLE` | OTP service is temporarily unavailable. |
| 500 | `INTERNAL_ERROR` | Unexpected server error. |

No `Retry-After` header or client countdown guarantee is established here.
The selected release's source configuration defaults include a 15-minute
access-token lifetime, 30-day refresh-token lifetime, five-minute OTP
expiration, and five send attempts per hour. These are source defaults, not
verified deployment values, and do not define UI timing promises; runtime
configuration may differ.

## Approved user experience and identity boundaries

The approved target is **email-only** sign-in: the user enters an email,
requests a code, enters that code, and becomes authenticated. New-user profile
completion follows the existing name flow. Keep inputs recoverable: invalid
input remains editable; invalid/expired codes allow correction or a deliberate
new request; rate-limit, temporary delivery, and network failures do not
trigger automatic resend loops. Suppress duplicate submissions while a request
is in progress. Editing the email invalidates the old challenge context so a
stale response cannot advance the newly edited flow.

These are target behavior requirements, not claims about the current UI. Exact
copy, presentation, and visual goldens belong to the separately planned client
implementation.

Email identity lookup does not implicitly attach an existing phone identity
or a profile email to a phone account. The V6 migration backfills `PHONE`
identities only; an unseen `EMAIL` identity can create a separate user.
Therefore:

- Email-only is the approved target, not evidence that no legacy phone users
  or sessions exist.
- Do not merge/link accounts, force logout, wipe tokens, reset user data, or
  silently fall back to phone login.
- If legacy users or rejected legacy sessions would be affected, stop that
  rollout and obtain an explicit owner-approved recovery decision first.

Preserve existing app data and available signed-in session state on upgrade.
Preserve Android `applicationId` `dev.whysoezzy.meetings` and its existing
signing identity. No signing-key change, package migration, uninstall, or
reset is authorized. If signing compatibility is unknown, upgrade certification
is blocked; do not treat a clean install as proof of an in-place upgrade.

## Current client gap and required downstream gates

The current MeetingsMP client is not compatible with the selected rotating
refresh contract: its manual and bearer refresh paths discard the replacement
refresh token. This documentation does not fix or conceal that incompatibility.
Before claiming compatibility, downstream implementation must persist the
returned access/refresh pair coherently, serialize concurrent refreshes, and
prevent an older refresh completion from restoring a session after logout.
Failure handling must distinguish transient network/service failure from an
authoritative rejected session and preserve the approved recovery decision.

Before rollout, downstream owners must discover actual Gradle tasks and retain
the project `AGENTS.md` gates, including detekt, `:meetings-shared:allTests`,
`:core:uikit:testDebugUnitTest`, and `assembleDebug`, plus SDK/auth common
tests. Prove affected Android and Desktop behavior, and iOS compilation/runtime
on an appropriate macOS/Xcode runner. UI changes require the project's
Roborazzi golden evidence. A signed same-package/same-signature upgrade must
cover new install, valid stored session, expired-access refresh, rejected
legacy session, and offline/failed refresh/logout scenarios. Include
concurrent-refresh/logout race checks and recovery behavior on the exact
implementation revision.

The product requires Android, iOS, Desktop, and Web (`wasmJs`), but Web is not
currently configured in this repository. Missing Web configuration/runner,
unavailable iOS or device runners, and missing signing artifacts are blocked
evidence, not passes. Android/JVM success alone does not prove iOS or Web
runtime compatibility. This documentation-only change has not run product
builds, tests, auth probes, signing checks, upgrade tests, or UI goldens.

The backend operations guide requires a coordinated database/schema cutover;
this client documentation authorizes no backend deployment or migration.
Client rollback cannot undo server schema or session changes. Documentation
rollback must revert this contract and its `AGENTS.md` pointer together.
