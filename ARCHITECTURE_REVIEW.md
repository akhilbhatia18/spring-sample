# Architecture Review Guide

Use this guide with any coding assistant or reviewer, including Claude and Grok.
For independent model feedback, run the review separately with each model and
compare their claims against the repository and its requirements.

## Reviewer instructions

Review the current architecture or the architectural impact of a proposed
change. Ground conclusions in repository evidence: inspect relevant source,
configuration, tests, build files, and documentation. Separate observed facts
from assumptions, and identify missing context. Do not modify files or commit
changes unless explicitly asked to implement a recommendation.

Assess the design in context, including:

- Component boundaries, responsibilities, dependencies, and layering.
- Data ownership, persistence, schema evolution, and transaction boundaries.
- API contracts, validation, error semantics, and compatibility.
- Security and privacy boundaries, identity, authorization, and sensitive data
  handling.
- Reliability, failure modes, concurrency, caching, and consistency.
- Performance and capacity implications for expected use.
- Configuration, deployment, observability, testability, and operability.
- Whether complexity is justified and whether the design follows established
  project patterns.

Do not treat a preference or an unverified future scale concern as a defect.
Explain trade-offs and distinguish concrete risks from optional improvements.
Do not claim that a design follows a standard unless the evidence supports it.

## Output format

1. **Scope and evidence** — state what was inspected and list significant
   limitations.
2. **Architecture summary** — describe the observed components and important
   data/control flows.
3. **Findings** — for each material risk, provide severity, affected
   component/file and line, evidence, consequences, and a practical
   recommendation.
4. **Trade-offs and open questions** — capture decisions that require product
   or operational context.
5. **Overall assessment** — state whether the design is appropriate for its
   stated purpose and why.

Use **Critical**, **High**, **Medium**, **Low**, or **Informational** for
severity. Rank findings by impact. Do not invent requirements; say when an
assessment depends on an unstated requirement.

## Suggested review prompt

> Review the supplied system or change using `ARCHITECTURE_REVIEW.md`. Base
> claims on repository evidence, distinguish facts from assumptions, and
> explain concrete trade-offs and risks. Provide the requested output sections.
> Do not edit files. Do not recommend a redesign without explaining the specific
> problem it solves.
