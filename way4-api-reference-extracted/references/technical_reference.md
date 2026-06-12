# Way4 SOAP Technical Reference

Thông tin kỹ thuật chung cho tất cả các Way4 SOAP APIs.

## SOAP Header
Mọi request đều chứa Header chuẩn:
```xml
<soapenv:Header>
   <wsin:SessionContextStr>?</wsin:SessionContextStr>
   <wsin:UserInfo>officer="WX_ADMIN"</wsin:UserInfo>
   <wsin:CorrelationId>?</wsin:CorrelationId>
</soapenv:Header>
```
- `UserInfo`: Định danh officer thực hiện (thường là `WX_ADMIN`).

## Search Methods (Client & Contract)

### ClientSearchMethod
- `CLIENT_NUMBER`: Số khách hàng (định dạng chuỗi số dài).
- `CLIENT_ID`: ID nội bộ hệ thống.

### ContractSearchMethod
- `CONTRACT_NUMBER`: Số hợp đồng hoặc số thẻ.
- `CONTRACT_ID`: ID nội bộ hệ thống.

## Common Return Codes
- `RetCode = 0`: Thành công.
- `RetMsg = Success` hoặc `I Successfully Completed`.

## Date Formats
- `YYYY-MM-DD`: Cho ngày sinh, ngày giao dịch.
- `YYMM`: Cho ngày hết hạn thẻ.
