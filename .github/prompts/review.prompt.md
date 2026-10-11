---
name: review
description: Review repository changes for code quality, architecture, and security using the project review guides.
argument-hint: Optional files, diff, or review focus
agent: agent
---

Review the requested scope using the repository's review guides:

- [Code review guide](../../CODE_REVIEW.md)
- [Architecture review guide](../../ARCHITECTURE_REVIEW.md)
- [Security review guide](../../SECURITY_REVIEW.md)

Use the user's stated scope if provided. Otherwise, review the current changes
against the appropriate base revision. If there are no identifiable changes,
review the files or scope active in the conversation; do not silently assume a
base revision. Read the relevant surrounding code, configuration, tests, and
documentation before reporting findings.

Treat the three guides as the review standards. Report only actionable,
evidence-backed findings introduced by the reviewed changes, with precise file
locations, concrete impact, severity, and confidence. For security findings,
include the realistic attack path, preconditions, and remediation; separate
hardening suggestions from demonstrated vulnerabilities. For architecture,
distinguish observed facts from assumptions and explain meaningful trade-offs.

Do not edit files, commit changes, or test a live system. Do not report
speculation, stylistic preferences, or unsupported dependency concerns. If no
findings are identified, state that clearly and disclose material scope or
validation limitations.

Organize the response into:

1. **Scope and limitations**
2. **Code findings**
3. **Architecture findings and trade-offs**
4. **Security findings**
5. **Overall assessment**

Order findings by severity and confidence. Use the severity and confidence
definitions in the referenced guides. Do not claim that Claude, Grok, or any
other independent model reviewed the changes unless that review was actually
performed.
