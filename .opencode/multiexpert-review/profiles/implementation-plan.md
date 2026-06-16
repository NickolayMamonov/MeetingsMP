---
name: implementation-plan
description: Default profile for implementation plans (plan-agent output, plan.md, conversation-described plans). Verdict alphabet PASS/CONDITIONAL/FAIL. Agents selected by tech-match.

detect:
  frontmatter_type: [implementation-plan, plan]
  path_globs:
    - "docs/plans/**"
  structural_signatures: []

reviewer_roster:
  primary: []          # empty → engine uses tech-match fallback (Step 2)
  optional_if:
    - when: "expect/actual|commonMain|iosMain|androidMain|desktopMain|multiplatform|\\bKMP\\b|Compose Multiplatform"
      agent: kmp-expert

allow_single_reviewer: true

verdicts: [PASS, CONDITIONAL, FAIL]

source_routing:
  plan: present-revised-plan
  file: edit-in-place
  conversation: inline-revise
---

## Rubric
Each reviewer applies their own expertise to:
- Scope of changes clearly described
- Architectural fit — modules, layers, dependency direction
- Technical approach sufficient to implement without further questions
- Risks named and addressed
- Trade-offs surfaced where multiple valid approaches exist
- Dependencies (code, libraries, services) identified
- Testing approach outlined (if implementation includes test code)

No fixed severity mapping — reviewers judge severity from their expertise.

## Prompt augmentation
(none — reviewers use the generic engine prompt)

## Agent pre-selection heuristic
`primary` is intentionally empty → tech-match selection. Scan plan content for technology
keywords, map to reviewer expertise, recommend 2–3 whose specialties the plan actually touches:
- security-expert only when the plan touches auth, encryption, tokens, secrets, or user data
- architecture-expert only when new modules, dependency-direction changes, or public API changes appear
- performance-expert only when hot paths, lists/pagination, coroutines, or heavy I/O appear
- kmp-expert whenever the plan touches commonMain/source sets, expect/actual, multiplatform libraries, Compose Multiplatform, or the iOS boundary
Prefer 2–3, but if only 1 is genuinely relevant, recommend 1 (allowed: `allow_single_reviewer: true`).
