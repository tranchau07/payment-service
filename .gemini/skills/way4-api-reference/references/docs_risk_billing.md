# Way4 Docs, Risk & Billing APIs

Tài liệu này tổng hợp cấu trúc request/response cho các API liên quan đến Tra cứu giao dịch, Quản lý rủi ro và Billing.

## Table of Contents
- [GetDocsByContractV2](#getdocsbycontractv2)
- [GetIssuingContractLastTransactionsV3](#getissuingcontractlasttransactionsv3)
- [GetRiskControlsByContract](#getriskcontrolsbycontract)
- [SetRiskControlTariffV2](#setriskcontroltariffv2)
- [GetBillingHistoryByContract](#getbillinghistorybycontract)

---

## GetDocsByContractV2
Lấy danh sách các tài liệu (giao dịch) của hợp đồng.
- **AuthFilterMode**: `A` (Authorized), v.v.
- Trả về `GenericDocDetailsV2APIRecord`.
- Chứa thông tin chi tiết: `TransAmount`, `TransDate`, `PostingStatus`, `ResponseCode`.

---

## GetIssuingContractLastTransactionsV3
Lấy N giao dịch gần nhất.
- **NDocs**: Số lượng giao dịch cần lấy.
- **Months**: Khoảng thời gian (tháng).

---

## GetRiskControlsByContract
Lấy danh sách các kiểm soát rủi ro (Limits) đang áp dụng.
- Trả về `RiskControlDetailsRecord`.
- Chứa: `Code` (e.g., `ATM_TRANS_DLY_USG`), `MaxAmount`, `MaxNumber`, `UsedAmount`, `AvailableAmount`.

---

## SetRiskControlTariffV2
Gán hoặc thay đổi hạn mức rủi ro.
- **TariffCode**: Mã hạn mức (e.g., `MCCG_ATM_DLY_USG`).
- **MaxAmount**, **MaxNumber**: Giá trị hạn mức mới.

---

## GetBillingHistoryByContract
Lấy lịch sử sao kê (Billing).
- **BillingEndDate**: Ngày kết thúc kỳ sao kê.
- Trả về `BillingHistoryDetailsRecord`.
