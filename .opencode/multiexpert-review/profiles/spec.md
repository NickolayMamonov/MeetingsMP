---
name: spec
description: Profile for feature specifications (docs/specs/<date>-<slug>.md). Panel of business-analyst + architecture-expert. Checks falsifiable AC, scope boundaries, explicit decisions, prerequisite realism.

detect:
  frontmatter_type: [spec]
  path_globs:
    - "docs/specs/**"
  structural_signatures: []

reviewer_roster:
  primary: [business-analyst, architecture-expert]
  optional_if:
    - when: "auth|token|encryption|PII|credential"
      agent: security-expert
    - when: "SLA|latency|throughput|budget|performance"
      agent: performance-expert
    - when: "a11y|accessibility|user-facing|UI|UX"
      agent: ux-expert

allow_single_reviewer: false

verdicts: [PASS, CONDITIONAL, FAIL]

severity_mapping:
  - items: [acceptance_criteria, prerequisites]
    severity: critical
  - items: [out_of_scope, decisions_made, affected_modules]
    severity: major
  - items: [open_questions_tagged, technical_approach_detail]
    severity: minor

source_routing:
  plan: N/A
  file: edit-in-place
  conversation: inline-revise
---

## Rubric

Reviewers evaluate the spec against these criteria. Each bullet carries the **item ID**
(matches `severity_mapping.items`) in parentheses — use the ID verbatim in every Issue title
stem so synthesizer aggregation stays greppable.

### Critical — spec is not implementable without these
- **(acceptance_criteria) Acceptance Criteria are falsifiable** — every AC is a grep-check,
  diff-check, parse-check, fixture-run, or structural-equivalence assertion. "Feels right" is
  not acceptable. An implementing agent must know unambiguously when each AC passes.
- **(prerequisites) Prerequisites realistic and complete** — every prerequisite has status
  (Done/Todo), owner (Human/Agent), and a concrete exit criterion. No hand-waved "everything is ready".

### Major — implementable but risky without these
- **(out_of_scope) Out of Scope is explicit** — an "Out of Scope" section enumerates what will
  NOT be done. Implied out-of-scope = violation.
- **(decisions_made) Decisions Made have rationale** — each locked decision has a "because Y".
  "We chose X" without rationale = violation.
- **(affected_modules) Affected modules/files complete** — a table listing every file touched
  with change type (New/Modified/Renamed/Deleted). Missing files → mid-implementation re-plan.

### Minor — implementable but less clear
- **(open_questions_tagged) Open questions tagged** blocking vs non-blocking.
- **(technical_approach_detail) Technical approach detail** — enough that the implementing agent
  needs no further research. High-level "use pattern X" without concrete locations/contracts = minor.

## Prompt augmentation

Evaluate the spec against the rubric above AND apply your general expertise (architecture-expert
checks dependency direction / module boundaries; business-analyst checks scope / requirements
consistency / user value).

**Issue title stem format (mandatory):** `(<item_id>) <violated | partial | satisfied>: <one-line summary>`.
Example: `(acceptance_criteria) violated: AC-R4 grep check unsatisfiable given AC-R6 whitelist`.
Unprefixed Issues fall back to reviewer-assigned severity, losing the profile's intended weighting.

## Verdict policy
- **PASS** — no critical issues, only minor suggestions.
- **CONDITIONAL** — no critical issues but major rubric items violated (fix strongly recommended before implementation).
- **FAIL** — any critical rubric item violated OR any blocker from reviewer expertise.

## No receipt
This profile writes no receipt; the verdict is a conversation-level output.
