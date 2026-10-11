# Security Review Guide

Use this guide with any coding assistant or reviewer, including Claude and Grok.
For independent model feedback, run the review separately with each model and
reconcile findings using verified evidence. This guide is a review procedure,
not a claim of security certification or compliance.

## Reviewer instructions

Inspect the change and the relevant surrounding code, configuration, build
dependencies, and tests. Trace untrusted input through the affected code to
sensitive operations and outputs. Consider how the application is configured
and deployed; do not assume a control exists unless the repository or supplied
evidence shows it. Do not modify files, exploit a live system, or commit changes
unless explicitly asked to do so.

Use applicable standards and threat-modeling practices as references, including
the OWASP Top 10, OWASP ASVS, and relevant CWE categories. Apply them to the
actual application rather than listing generic checklist items.

Consider, where applicable:

- Authentication, authorization, privilege boundaries, and insecure direct
  object references.
- Input validation, injection, deserialization, path handling, and output
  encoding.
- Secrets, personal data, logging, storage, encryption, and data retention.
- Session and token handling, CSRF, CORS, and security headers.
- Dependency and supply-chain risks supported by dependency or advisory
  evidence.
- Server-side request forgery, file upload/download, and unsafe redirects.
- Denial of service, resource exhaustion, race conditions, and unsafe defaults.
- Error responses and whether they expose sensitive details.

Only report a vulnerability when there is a credible attack path and security
impact. State required attacker access, preconditions, affected configuration,
and practical impact. Do not report missing controls that are irrelevant to
the application's exposure, or vulnerabilities based only on a dependency's
presence without evidence of an affected version and reachable vulnerable
functionality. If exploitability cannot be determined, say what evidence is
missing.

## Severity and confidence

- **Critical** — Severe compromise or impact is practical, with little or no
  special precondition.
- **High** — Significant compromise is practical under realistic conditions.
- **Medium** — Meaningful impact requires specific access, configuration, or
  user interaction.
- **Low** — Limited impact or a narrow, difficult-to-exploit condition.
- **Informational** — A security-hardening observation without a demonstrated
  vulnerability; keep these separate from vulnerabilities.

Provide a confidence score from 1 to 10. Confidence reflects evidence and
exploitability certainty, not severity.

## Output format

Start with a concise scope statement and any limitations. Then list
vulnerabilities in descending severity and confidence using this table:

| # | Severity | File | Lines | Vulnerability | Attack path and impact | Recommendation | Confidence |
| --- | --- | --- | --- | --- | --- | --- | --- |

Use exact file paths and line numbers or a short line range. Include the
attacker's required access and concrete evidence for every vulnerability.
List hardening opportunities separately as informational observations. If no
vulnerabilities are identified, state that this does not prove the application
is secure and mention what was not assessed.

## Suggested review prompt

> Perform a security review using `SECURITY_REVIEW.md`. Inspect relevant code
> and configuration, trace realistic attack paths, and report only
> evidence-backed vulnerabilities with exact locations, preconditions, impact,
> severity, remediation, and confidence. Separate hardening suggestions from
> vulnerabilities. Do not edit files or test a live system. State the review
> scope and limitations; do not claim certification or that the system is
> secure merely because no finding was identified.
