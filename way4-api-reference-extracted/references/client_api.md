# Way4 Client APIs

Tài liệu này tổng hợp cấu trúc request/response cho các API liên quan đến Client.

## Table of Contents
- [CreateClientV4](#createclientv4)
- [EditClientV6](#editclientv6)
- [GetClientByParmsV2](#getclientbyparmsv2)
- [CreateClientAddressV2](#createclientaddressv2)
- [EditClientAddressV3](#editclientaddressv3)
- [GetClientClassifiersV3](#getclientclassifiersv3)
- [SetClientClassifier](#setclientclassifier)
- [SetCustomClientData](#setcustomclientdata)

---

## CreateClientV4
Dùng để tạo khách hàng mới.

### Request Highlights
- **SearchMethod**: Thường không dùng vì là tạo mới, nhưng có `InstitutionCode`, `Branch`.
- **ClientTypeCode**: `PR` (Private Resident).
- **IdentityCardNumber**: CMND/CCCD.
- **ClientNumber**: Số khách hàng định nghĩa trước.
- **SetCustomData_InObject**: Có thể truyền nhiều tag (e.g., `PrevID_01`).

### Example
```xml
<wsin:CreateClientV4>
   <wsin:CreateClient_InObject>
      <wsin:InstitutionCode>0001</wsin:InstitutionCode>
      <wsin:Branch>001</wsin:Branch>
      <wsin:ClientTypeCode>PR</wsin:ClientTypeCode>
      <wsin:ShortName>Truong Ha Anh</wsin:ShortName>
      <wsin:FirstName>Anh</wsin:FirstName>
      <wsin:LastName>Truong</wsin:LastName>
      <wsin:BirthDate>2000-12-12</wsin:BirthDate>
      <wsin:Gender>F</wsin:Gender>
      <wsin:IdentityCardNumber>0997765539</wsin:IdentityCardNumber>
      <wsin:ClientNumber>099773245419</wsin:ClientNumber>
   </wsin:CreateClient_InObject>
</wsin:CreateClientV4>
```

### Response
- **NewClient**: ID khách hàng trong hệ thống.
- **ApplicationNumber**: Số application.

---

## GetClientByParmsV2
Tìm kiếm thông tin chi tiết của khách hàng.

### Search Methods
- `CLIENT_ID`: Dùng ID nội bộ (NewClient).
- `CLIENT_NUMBER`: Dùng số khách hàng.

### Example Response
Trả về `IssClientDetailsV2APIRecord` với đầy đủ thông tin cá nhân, địa chỉ và `AddInfo01` (Custom data tags).

---

## SetClientClassifier
Gán phân loại cho khách hàng.
- **Classifier**: `CLIENT_SEGMENT`, `BRANCH`, v.v.
- **Value**: Mã phân loại (e.g., `SEG_18`).
