# Way4 Product Mappings

This reference contains the mapping of Product Identifiers to their respective categories (PCAT/CON_CAT) and their roles in the system.

| Internal Code | Name | CON_CAT | PCAT | Suggested Role |
| :--- | :--- | :--- | :--- | :--- |
| `161103000000000000000045` | ATM_ACC | A | M | Potential Parent (Account) |
| `250610000000000000000400` | TRAINING03 | A | C | Potential Parent/Child |
| `250610000000000000000410` | TRAINING03_01 | C | C | Child (Card) |

## Category Definitions
- **CON_CAT (Contract Category):**
    - `A`: Account (Quản lý tiền/hạn mức)
    - `C`: Card (Thẻ vật lý/logic)
- **PCAT (Product Category):**
    - `C`: Issuing
    - `M`: Acquiring
    - `B`: Bank Accounting
