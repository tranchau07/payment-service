# Way4 Instalment APIs

Tài liệu này tổng hợp cấu trúc request/response cho các API liên quan đến Trả góp (Instalment).

## Table of Contents
- [CreateInstalmentPlanByBalance](#createinstalmentplanbybalance)
- [CreateInstalmentPlanByTransaction](#createinstalmentplanbytransaction)
- [GetInstalmentPlansByContract](#getinstalmentplansbycontract)
- [GetInstalmentPortionsByPlanID](#getinstalmentportionsbyplanid)
- [InstFullEarlyRepayment](#instfullearlyrepayment)

---

## CreateInstalmentPlanByBalance
Tạo kế hoạch trả góp dựa trên dư nợ một mã Balance.
- **InstalmentServiceCode**: Ví dụ `SVFC_CASH`.
- **BalanceCode**: Ví dụ `OPEN_CASH`.
- **Tenor**: Số kỳ trả góp (e.g., `12`).

---

## CreateInstalmentPlanByTransaction
Tạo trả góp cho một giao dịch cụ thể.
- **DocumentID**: ID của giao dịch cần trả góp.

---

## GetInstalmentPlansByContract
Lấy danh sách các kế hoạch trả góp của hợp đồng.
- **InstalmentPlanState**: `ACTIVE`, `WAITING`, v.v.

---

## GetInstalmentPortionsByPlanID
Lấy chi tiết các kỳ trả góp của một kế hoạch.
- Trả về danh sách `InstalmentPortionDetailsAPIRecord`.
- Mỗi record chứa: `PortionNumber` (e.g., `01/12`), `DueDate`, `PortionAmount`, `PrincipalAmount`, `FeeAmount`.

---

## InstFullEarlyRepayment
Tất toán sớm toàn bộ khoản trả góp.
- **InstChainIDT**: ID của chuỗi trả góp.
- **RepaymentDate**: Ngày tất toán.
