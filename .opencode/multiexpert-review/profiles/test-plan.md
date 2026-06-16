---
name: test-plan
description: Profile for test-plan artifacts (docs/testplans/<slug>-test-plan.md). Verdict alphabet PASS/WARN/FAIL with a 7-item checklist (a–g). Primary reviewer business-analyst; adds domain specialists when the linked spec invokes their concerns.

detect:
  frontmatter_type: [test-plan, test-plan-receipt]
  path_globs:
    - "docs/testplans/**"
    - "swarm-report/*-test-plan.md"
  structural_signatures:
    - "^## Test Cases"
    - "^#{1,6}\\s+TC-[\\w-]+"
    - "P[0-3]"

reviewer_roster:
  primary: [business-analyst]
  optional_if:
    - when: "auth|token|encryption|PII|credential"
      agent: security-expert
    - when: "SLA|latency|throughput|budget"
      agent: performance-expert
    - when: "a11y|accessibility"
      agent: ux-expert

allow_single_reviewer: true

verdicts: [PASS, WARN, FAIL]

severity_mapping:
  - items: ["a", "b", "c", "f"]
    severity: critical
  - items: ["d", "e", "g"]
    severity: major

source_routing:
  plan: N/A
  file: edit-in-place
  conversation: N/A

receipt:
  path_template: "swarm-report/<slug>-test-plan.md"
  fields_to_update: [review_verdict, review_warnings, review_blockers]
---

## Rubric

Every reviewer evaluates the test-plan against these seven items and reports the status of each
explicitly. The engine copies the items verbatim into the review prompt (see Prompt augmentation).

- **(a) AC coverage** — every Acceptance Criterion from the linked spec has ≥1 Test Case verifying it.
- **(b) Negative balance** — every happy-path TC has ≥2 unhappy/negative TCs on the same flow (invalid input, error states, boundary, race). Mostly-happy-path = violation.
- **(c) Edge cases present** — ≥1 TC explicitly tagged as an edge case (boundary, empty/null, max size, tz/locale, concurrency, resource exhaustion). None at all = violation.
- **(d) Non-functional where applicable** — if the spec mentions {SLA, latency, throughput, a11y, auth, encryption, PII, resource/rate limits}, ≥1 non-functional TC covers it. If the spec mentions none, trivially satisfied.
- **(e) Priority-risk alignment** — P0–P3 consistent with risk: high-risk flows (data loss, auth, payment, destructive) at P0–P1; user-facing critical paths at P0–P1; trivial at P2–P3.
- **(f) Type field present and valid** — every TC declares `Type` ∈ {unit, integration, ui-instrumentation, ui-scenario, screenshot, e2e} with a non-empty one-line rationale. Missing/unknown/empty = violation. (Reviewers check presence + plausibility, do not re-classify.)
- **(g) Instrumentation declared** — when user-facing/prod-bound or touching an observability hot-path (network, payments, background jobs, auth, migrations), the plan ends with `## Non-functional / Instrumentation` listing Log events / Metrics / Traces / Alerts / Dashboards. For internal/dev-only/refactor, an explicit one-line `N/A: <reason>` is acceptable. Missing or `TBD`/`?`/blank = violation.

## Verdict policy

| Verdict | Trigger | Exit |
|---------|---------|------|
| **FAIL** | Any of (a), (b), (c), (f) violated | Revise-loop, max 3 cycles; after 3 still FAIL → escalate. Pipeline blocked. |
| **WARN** | (a)(b)(c)(f) satisfied, but (d), (e), or (g) violated | Pipeline continues; record `review_verdict: WARN` + violated items. No revise-loop. |
| **PASS** | All seven satisfied | Pipeline continues; record `review_verdict: PASS`. |

A single critical from any agent with medium-or-higher confidence triggers FAIL.

## Prompt augmentation

The engine substitutes this section literally into `{PROFILE_PROMPT_AUGMENTATION}`. Each agent
must explicitly report the status (satisfied / violated, with rationale) of every item.

---

**Test-plan rubric — evaluate each item explicitly:**

- **(a) AC coverage** — every Acceptance Criterion from the linked spec has ≥1 Test Case that verifies it. Missing or weak mapping is a violation.
- **(b) Negative balance** — every happy-path TC has ≥2 unhappy/negative TCs covering the same flow (invalid input, error states, boundary violations, concurrent/race conditions). Mostly happy paths = violation.
- **(c) Edge cases present** — at least one TC explicitly tagged as an edge case (boundary value, empty/null, maximum size, timezone/locale, concurrency, resource exhaustion). No edge-case TC at all = violation.
- **(d) Non-functional scenarios where applicable** — if the linked spec mentions any of {SLA, latency budget, throughput, a11y, auth, encryption, PII, resource limits, rate limits}, there must be ≥1 non-functional TC covering that concern. If the spec mentions none, trivially satisfied.
- **(e) Priority-risk alignment** — priorities (P0–P3) consistent with risk: high-risk flows (data loss, auth, payment, destructive) at P0–P1; user-facing critical paths at P0–P1; trivial/informational at P2–P3. Mismatch = violation.
- **(f) Type field present and valid** — every Test Case declares an explicit `Type` from {unit, integration, ui-instrumentation, ui-scenario, screenshot, e2e} with a non-empty one-line `Type rationale`. Missing, unknown value, or empty rationale = violation.
- **(g) Instrumentation declared** — when the spec/task is user-facing or prod-bound, or touches an observability hot-path (network calls, payments, background jobs, auth, data migrations), the plan ends with a `## Non-functional / Instrumentation` section listing Log events / Metrics / Traces / Alerts / Dashboards. For internal/dev-only/refactor work, an explicit `N/A: <reason>` is acceptable. Missing, or `TBD`/`?`/blank = violation.

For every Issue you raise, use the item ID as the title stem — e.g. `(a) AC coverage: API X has no test case`.

## Receipt integration

After synthesis, the engine updates `swarm-report/<slug>-test-plan.md` (the receipt, not the
permanent file at `docs/testplans/<slug>-test-plan.md`) with:
- `review_verdict: PASS | WARN | FAIL`
- On WARN: `review_warnings:` — violated items from (d), (e), (g), one-line rationale each.
- On FAIL: `review_blockers:` — violated items from (a), (b), (c), (f), with the blocking finding + suggested fix.

If the receipt file is missing, create it. The "Fix Plan" action in the revise-loop edits the
permanent file at `docs/testplans/<slug>-test-plan.md`, not the receipt.
