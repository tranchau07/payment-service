# Liability Constraints Matrix

This reference defines the business rules and constraints for each `LiabCategory` when creating contracts.

## Liability Categories

### Y - Full Liability
- **Definition:** Child contract shares the balance and limit of the parent.
- **Constraints:**
    - `Currency` MUST match Parent's currency exactly.
    - `Branch` usually matches Parent's branch but can be overridden.
    - Child does not have its own `ACNT_BALANCE` records for primary funds.

### N - Affiliated
- **Definition:** Child contract is linked for identification but has its own balance.
- **Constraints:**
    - `Currency` can differ from Parent.
    - Child has independent `ACNT_BALANCE`.

### A - Only Check Balance
- **Definition:** Transactions on the child contract are validated against the parent's balance.
- **Constraints:**
    - Requires specific limit configurations in `AddInfo` tags if enforced.

### R - Reporting
- **Definition:** Linkage only for grouping in reports.
- **Constraints:**
    - No financial impact or constraints on currency.

## General Field Constraints
- **ApplRegNumber:** Must be unique for each request to prevent duplicates.
- **Branch/InstitutionCode:** Must be valid codes within the operating environment (e.g., `0101`, `0001`).
