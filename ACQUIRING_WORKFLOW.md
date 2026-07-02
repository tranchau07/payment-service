# Acquiring idempotency and recovery

All acquiring write APIs require an `Idempotency-Key` header (16-128 safe characters):

- `POST /api/merchants/register`
- `POST /api/contracts/acquiring`
- `POST /api/contracts/acquiring/{contractNumber}/addresses`
- `POST /api/contracts/acquiring/{contractNumber}/devices`

The same key and payload replay the saved successful response without calling WAY4 again. Reusing a key with another payload returns `IDM_01`.

Workflow records are JSON files written atomically outside the WAY4 database. Configure a persistent directory:

```text
ACQUIRING_WORKFLOW_DIR=/var/lib/payment-service/acquiring-workflows
```

For multiple application instances, mount the same filesystem. Per-key OS file locks prevent concurrent execution across instances sharing that volume.

When a SOAP timeout, connection failure, or response parsing failure occurs, the record becomes `OUTCOME_UNKNOWN` and the same key is blocked with `IDM_03`. Do not create a new key until the operation has been reconciled in WAY4 using its natural identifier (client number, merchant ID, contract number, CBS number, or device serial).

The initiating user can inspect the journal state through:

```http
GET /api/acquiring-workflows/{idempotencyKey}
```

No table or schema change in WAY4 is required.
