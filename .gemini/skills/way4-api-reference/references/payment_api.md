# Way4 Payment APIs

Tài liệu này tổng hợp cấu trúc request/response cho các API liên quan đến Giao dịch Thanh toán (Payment).

## Table of Contents
- [IssPaymentToContract](#isspaymenttocontract)
- [IssPaymentFromContract](#isspaymentfromcontract)
- [IssPaymentToContractRev](#isspaymenttocontractrev)
- [IssPaymentFromContractRev](#isspaymentfromcontractrev)

---

## IssPaymentToContract
Nộp tiền vào tài khoản/hợp đồng (Credit).
- **UniqueRefNumber**: Mã tham chiếu duy nhất từ hệ thống gọi.
- **MsgCode**: `PAYACC`.
- **RRN**: Retrieval Reference Number.
- **SourceContractNumber**: Thường là tài khoản trung gian (e.g., `001-TELLER`).

---

## IssPaymentFromContract
Rút tiền/Thanh toán từ tài khoản/hợp đồng (Debit).
- **MsgCode**: `PAYFACC`.

---

## IssPaymentToContractRev / IssPaymentFromContractRev
Đảo giao dịch (Reversal).
- **MsgCode**: `PAYACC_REV` hoặc `PAYFACC_REV`.
- **RRN**: Phải khớp với RRN của giao dịch gốc cần đảo.
