---
name: way4-api-reference
description: Chuyên gia về cấu trúc request/response SOAP của hệ thống Way4 (OpenWay). Sử dụng khi cần tra cứu các trường dữ liệu, phương thức tìm kiếm (SearchMethod) và ví dụ payload cho các API Client, Contract, Card, Instalment, Payment và Risk Controls.
---

# Way4 API Reference Skill

Kỹ năng này cung cấp hướng dẫn chi tiết về cách tương tác với các SOAP APIs của hệ thống Way4 (OpenWay). Nó dựa trên phân tích các mẫu API (Request/Response) thực tế từ hệ thống.

## Các nhóm API chính

Tra cứu chi tiết cấu trúc và ví dụ cho từng nhóm API tại các tài liệu tham khảo dưới đây:

1.  **Thông tin chung**: [technical_reference.md](references/technical_reference.md)
    - Cấu trúc SOAP Header, UserInfo.
    - Các phương thức tìm kiếm (SearchMethods) phổ biến.
    - Định dạng ngày tháng và mã lỗi.

2.  **Client (Khách hàng)**: [client_api.md](references/client_api.md)
    - Tạo/Sửa khách hàng, gán phân loại (Classifier), địa chỉ.

3.  **Contract (Hợp đồng)**: [contract_api.md](references/contract_api.md)
    - Tạo hợp đồng Liability, hợp đồng phát hành, tra cứu thông tin tài chính.

4.  **Card (Thẻ)**: [card_api.md](references/card_api.md)
    - Tạo thẻ, kích hoạt, đổi mã PIN, phát hành lại.

5.  **Instalment (Trả góp)**: [instalment_api.md](references/instalment_api.md)
    - Tạo trả góp theo dư nợ/giao dịch, tất toán sớm, tra cứu kỳ trả góp.

6.  **Payment (Giao dịch)**: [payment_api.md](references/payment_api.md)
    - Nộp tiền, rút tiền, đảo giao dịch (Reversal).

7.  **Risk & Billing**: [docs_risk_billing.md](references/docs_risk_billing.md)
    - Tra cứu lịch sử giao dịch, quản lý hạn mức rủi ro, lịch sử sao kê.

## Nguyên tắc sử dụng

- Luôn kiểm tra `SearchMethod` (ví dụ `CONTRACT_NUMBER`) để đảm bảo khớp với `Identifier` được cung cấp.
- Khi build payload cho `Edit` APIs, chú ý các trường có thể xóa bằng `FieldsToClear`.
- Tham chiếu các ví dụ trong tài liệu để đảm bảo đúng namespace (`wsin:`) và cấu trúc phân cấp XML.
