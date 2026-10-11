# Code Review Guide

Use this guide with any coding assistant or reviewer, including Claude and Grok.
For independent model feedback, run the review separately with each model and
compare only evidence-backed findings.

## Reviewer instructions

Review the proposed changes against the target branch or the explicitly supplied
scope. Read the relevant surrounding code, tests, and project conventions before
reporting a finding. Do not modify files or commit changes unless explicitly
asked to implement fixes.

Focus on correctness and regressions, including:

- Incorrect or incomplete behavior, edge cases, and error handling.
- API or data compatibility changes and violations of existing contracts.
- Concurrency, transaction, persistence, and cache consistency.
- Missing or ineffective tests for changed behavior.
- Resource leaks, performance problems, and maintainability issues that have a
  concrete impact.

Do not report stylistic preferences, speculative concerns, or issues unrelated
to the changes. Do not infer behavior from filenames or comments when it can be
verified in code. If relevant context is unavailable, state the limitation
instead of presenting an assumption as a fact.

## Output format

Report only actionable findings, ordered by severity and then confidence. For
each finding include:

| Severity | Location | Finding | Impact | Evidence | Confidence |
| --- | --- | --- | --- | --- | --- |

Use a precise file path and line number (or a short line range). Explain the
triggering conditions and user-visible or operational impact. Suggest a focused
remediation where practical. If no issues are found, say so and mention the
scope reviewed and any material validation not performed.

Severity:

- **Critical** — A change enables a severe, broadly exploitable or destructive
  failure with no reasonable preconditions.
- **High** — A change can cause serious impact in realistic conditions or
  breaks a core application capability.
- **Medium** — A change causes meaningful but bounded impact, or requires
  particular conditions.
- **Low** — A minor defect with limited impact.

Confidence is a score from 1 to 10 describing confidence that the finding is
real and caused by the reviewed changes; it is not a severity score.

## Suggested review prompt

> Review the supplied code changes using `CODE_REVIEW.md`. Inspect relevant
> surrounding code and tests. Report only actionable defects introduced by the
> changes, with exact file/line locations, impact, evidence, severity, and
> confidence. Do not edit files. If there are no findings, state that clearly
> and disclose important validation limitations.
