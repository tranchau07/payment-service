# Contract SOAP to DB Mapping

Bảng đối chiếu giữa các trường trong SOAP API `CreateContractV4` / `GetContractV2` và bảng `ACNT_CONTRACT` trong cơ sở dữ liệu.

| SOAP Tag (wsin:) | DB Column (Table: ACNT_CONTRACT) | Data Type | Description |
| :--- | :--- | :--- | :--- |
| `ContractNumber` | `CONTRACT_NUMBER` | VARCHAR2(64) | Số hợp đồng/Số thẻ |
| `ContractName` | `CONTRACT_NAME` | VARCHAR2(255) | Tên hợp đồng |
| `ProductCode` | `PRODUCT` | VARCHAR2(32) | Mã sản phẩm (Code) |
| `Currency` | `CURR` | VARCHAR2(3) | Loại tiền tệ (Số/Chữ) |
| `CreditLimit` | `AUTH_LIMIT_AMOUNT` | NUMBER | Hạn mức tín dụng |
| `Balance` | `TOTAL_BALANCE` | NUMBER | Dư nợ tổng tại thời điểm hiện tại |
| `Available` | `AMOUNT_AVAILABLE` | NUMBER | Số dư khả dụng |
| `OpenDate` | `DATE_OPEN` | DATE | Ngày mở hợp đồng |
| `ExpirationDate` | `DATE_EXPIRE` | DATE | Ngày hết hạn |
| `CBSNumber` | `RBS_NUMBER` | VARCHAR2(64) | Số tài khoản Core Banking |
| `LiabCategory` | `LIAB_CATEGORY` | VARCHAR2(1) | Loại Liability (Y/N) |
| `Status` | `CONTR_STATUS` | NUMBER | Trạng thái hợp đồng (ID) |

## Contract Hierarchy (Phân cấp)
Hệ thống Way4 sử dụng cột `CONTRACT_LEVEL` để quản lý phân cấp:
- `.1.` : Hợp đồng cha (Top level/Liability).
- `.1.1.` : Hợp đồng con (Issuing/Card).
- `.1.1.1.` : Hợp đồng phụ (Supplementary).
- ID của hợp đồng cha được lưu tại cột `LIAB_CONTRACT`.
