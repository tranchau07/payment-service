---
name: way4-business-rules
description: Chuyên gia về quy tắc nghiệp vụ và ràng buộc dữ liệu trong hệ thống Way4. Sử dụng khi cần validate SOAP request, mapping Product/Client/Contract hoặc tra cứu mối quan hệ Liability.
---

# Way4 Business Rules Skill

Kỹ năng này tập trung vào việc quản lý và áp dụng các quy tắc nghiệp vụ đặc thù của hệ thống Way4 cho dự án Payment Service.

## Quy trình Validate Request (CreateIssuingContractWithLiabilityV2)

Khi nhận được yêu cầu tạo hợp đồng với Liability, hãy thực hiện các bước sau:

1.  **Xác định LiabCategory:** Kiểm tra giá trị của `<wsin:LiabCategory>`.
2.  **Kiểm tra ràng buộc Currency:** 
    - Nếu là `Y`, đối chiếu `Currency` trong `InObject` với đồng tiền của hợp đồng cha.
    - Tham khảo: [liability_constraints.md](references/liability_constraints.md)
3.  **Xác thực Product:**
    - Kiểm tra `ProductIdentifier` để biết loại sản phẩm (Card/Account).
    - Tham khảo: [product_mappings.md](references/product_mappings.md)
4.  **Kiểm tra định danh:** Đảm bảo `SearchMethod` khớp với định dạng của `Identifier`.

## Tài liệu tham khảo
- [Quy tắc Liability](references/liability_constraints.md)
- [Mapping Sản phẩm](references/product_mappings.md)

## Ví dụ SOAP chuẩn
Dùng để đối chiếu khi build payload:
```xml
<wsin:CreateIssuingContractWithLiabilityV2>
    <wsin:LiabCategory>Y</wsin:LiabCategory>
    <wsin:LiabContractSearchMethod>CONTRACT_NUMBER</wsin:LiabContractSearchMethod>
    <wsin:LiabContractIdentifier>001-P-354274</wsin:LiabContractIdentifier>
    <wsin:ProductIdentifier>250610000000000000000410</wsin:ProductIdentifier>
    ...
</wsin:CreateIssuingContractWithLiabilityV2>
```
