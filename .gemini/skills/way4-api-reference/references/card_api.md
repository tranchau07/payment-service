# Way4 Card APIs

Tài liệu này tổng hợp cấu trúc request/response cho các API liên quan đến Thẻ (Card).

## Table of Contents
- [CreateCardV3](#createcardv3)
- [ActivateCard](#activatecard)
- [SetCardStatus](#setcardstatus)
- [AcqSetPin / AcqChangePin](#acqsetpin-acqchangepin)
- [ReissueCard](#reissuecard)

---

## CreateCardV3
Tạo thẻ vật lý gắn với hợp đồng thẻ.
- **ProductCode**: Ví dụ `MC_CR_GLD_M` (Main Card).
- **InObject**: Chứa tên in trên thẻ (`EmbossedFirstName`, `EmbossedLastName`).

---

## ActivateCard / CancelActivation
Kích hoạt hoặc hủy kích hoạt thẻ.
- **ContractIdentifier**: Số thẻ.
- **Reason**: Lý do thực hiện.

---

## SetCardStatus
Thay đổi trạng thái thẻ (e.g., khóa thẻ).
- **NewStatus**: Mã trạng thái mới (e.g., `CCL01` - Card Closed).

---

## AcqSetPin / AcqChangePin
Thiết lập hoặc đổi PIN qua kênh Acquisition.
- **NewPinBlock**: Block mã PIN mới đã mã hóa.
- **SourceContractNumber**: Thường là số hiệu Virtual POS (e.g., `00004567`).

---

## ReissueCard
Phát hành lại thẻ.
- **ProductionReason**: `RALL` (Renew All).
- **ExpirationDate**: Ngày hết hạn mới (format `YYMM`).
