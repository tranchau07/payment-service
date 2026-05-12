# Client SOAP to DB Mapping

Bảng đối chiếu giữa các trường trong SOAP API `CreateClientV4` / `EditClientV6` và bảng `CLIENT` trong cơ sở dữ liệu.

| SOAP Tag (wsin:) | DB Column (Table: CLIENT) | Data Type | Description |
| :--- | :--- | :--- | :--- |
| `InstitutionCode` | `F_I` (Mapped via Parent ID) | NUMBER | Mã định chế tài chính |
| `Branch` | `BRANCH` | VARCHAR2(32) | Chi nhánh quản lý khách hàng |
| `ClientTypeCode` | `CLT` (Mapped via ID) | NUMBER | Loại khách hàng (PR, v.v.) |
| `ShortName` | `SHORT_NAME` | VARCHAR2(255) | Tên ngắn gọn |
| `FirstName` | `FIRST_NAM` | VARCHAR2(255) | Tên |
| `LastName` | `LAST_NAM` | VARCHAR2(255) | Họ |
| `MiddleName` | `FATHER_S_NAM` | VARCHAR2(255) | Tên đệm (thường map vào đây) |
| `BirthDate` | `BIRTH_DATE` | DATE | Ngày sinh |
| `Gender` | `GENDER` | VARCHAR2(1) | Giới tính (M/F) |
| `SocialSecurityNumber`| `SOCIAL_NUMBER` | VARCHAR2(64) | Số an sinh xã hội |
| `IdentityCardNumber` | `REG_NUMBER` | VARCHAR2(64) | Số CMND/CCCD |
| `IdentityCardDetails`| `REG_DETAILS` | VARCHAR2(255) | Nơi cấp/Ngày cấp CMND |
| `ClientNumber` | `CLIENT_NUMBER` | VARCHAR2(64) | Mã khách hàng (duy nhất) |
| `AddressLine1` | `ADDRESS_LINE_1` | VARCHAR2(255) | Địa chỉ dòng 1 |
| `City` | `CITY` | VARCHAR2(255) | Thành phố |
| `MobilePhone` | `PHONE_M` | VARCHAR2(32) | Số điện thoại di động |
| `EMail` | `E_MAIL` | VARCHAR2(255) | Địa chỉ Email |

## Custom Data
Các trường `SetCustomData_InObject` trong SOAP được lưu trữ vào các cột:
- `ADD_INFO_01`
- `ADD_INFO_02`
- `ADD_INFO_03`
- `ADD_INFO_04`
(Dưới dạng chuỗi `TAG1=VALUE1;TAG2=VALUE2;`)
