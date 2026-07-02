# Product configuration API

The API is read-only and does not create or modify any WAY4 table.

## Business classification

- `APPL_PRODUCT.PCAT = 'C'`: Issuing
- `APPL_PRODUCT.PCAT = 'M'`: Acquiring
- `CON_CAT` describes the contract level/category and is not used to split Issuing and Acquiring.

By default only records with `AMND_STATE='A'` and `IS_READY='Y'` are returned. Pass `includeNotReady=true` to include active products that are not ready.

## Endpoints

```http
GET /api/product-configurations/issuing
GET /api/product-configurations/acquiring
GET /api/product-configurations/{productCode}
```

Example:

```json
{
  "code": "RETAIL_TERMINAL",
  "businessType": "ACQUIRING",
  "pcat": "M",
  "conCat": "M",
  "rootProduct": false,
  "contractType": {
    "id": 19,
    "code": "PO",
    "name": "Our POS",
    "displayName": "PO - Our POS",
    "sourceTable": "CONTR_TYPE"
  },
  "contractSubtype": {
    "id": 21,
    "code": "PO-C",
    "sourceTable": "CONTR_SUBTYPE"
  },
  "accountScheme": {
    "id": 42,
    "code": "MR",
    "sourceTable": "ACC_SCHEME"
  },
  "servicePack": {
    "id": 1521,
    "code": "CAPR-UL",
    "sourceTable": "SERV_PACK"
  },
  "missingReferences": []
}
```

`missingReferences` contains values such as `CONTR_SUBTYPE:123` when an APPL_PRODUCT foreign identifier points to a missing or inactive catalog record. This lets the UI show a configuration warning without hiding the product.
