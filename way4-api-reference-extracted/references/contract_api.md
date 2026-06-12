# Way4 Contract APIs

Tài liệu này tổng hợp cấu trúc request/response cho các API liên quan đến Contract (Hợp đồng).

## Table of Contents
- [CreateContractV4](#createcontractv4)
- [GetContractV2](#getcontractv2)
- [GetContractsByClientV2](#getcontractsbyclientv2)
- [SetContractClassifier](#setcontractclassifier)
- [SetContractCreditLimit](#setcontractcreditlimit)
- [SetCustomContractData](#setcustomcontractdata)

---

## CreateContractV4
Tạo hợp đồng mới (thường là Liability Contract).

### Request
- **ClientSearchMethod**: `CLIENT_NUMBER`, `CLIENT_ID`.
- **ProductCode**: Ví dụ `ISS_CR_P_LIB` (Liability Contract).
- **CBSNumber**: Số tài khoản Core Banking.

### Example
```xml
<wsin:CreateContractV4>
   <wsin:ClientSearchMethod>CLIENT_NUMBER</wsin:ClientSearchMethod>
   <wsin:ClientIdentifier>099773245419</wsin:ClientIdentifier>
   <wsin:CreateContract_InObject>
      <wsin:Branch>001</wsin:Branch>
      <wsin:ProductCode>ISS_CR_P_LIB</wsin:ProductCode>
      <wsin:ContractName>Liability Contract</wsin:ContractName>
      <wsin:CBSNumber>21324556600</wsin:CBSNumber>        
   </wsin:CreateContract_InObject>
</wsin:CreateContractV4>
```

---

## GetContractV2 / GetContractByNumberV2
Lấy thông tin chi tiết hợp đồng.

### Response Highlights
- **ContractCategory**: `A` (Account), `C` (Card).
- **LiabilityCategory**: `Y` (Full Liability).
- **Available**, **Balance**, **CreditLimit**: Thông tin tài chính.
- **ContractLevel**: `.1.` (Level 1), `.1.1.` (Level 2 - con của level 1).
- **AddInfo01/02**: Chứa các tag nghiệp vụ (e.g., `PAYMENT_OPTION=MTP`).

---

## CreateIssuingContractWithLiabilityV2
Tạo hợp đồng phát hành có liên kết với hợp đồng Liability.
- **LiabCategory**: `Y` (Liên kết đầy đủ).
- **LiabContractIdentifier**: Số hợp đồng Liability cha.
- **ProductCode**: Ví dụ `MC_CR_GLD` (MasterCard Credit Gold).
