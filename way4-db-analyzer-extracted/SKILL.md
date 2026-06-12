---
name: way4-db-analyzer
description: Chuyên gia phân tích cơ sở dữ liệu Way4 (Oracle). Sử dụng khi cần mapping các tag SOAP API sang bảng/cột DB, tra cứu thông tin kết nối từ application.yaml, hoặc thực hiện các truy vấn kiểm tra dữ liệu thực tế trong DB.
---

# Way4 DB Analyzer Skill

Kỹ năng này giúp bạn hiểu sâu về cấu trúc dữ liệu bên dưới của hệ thống Way4, hỗ trợ việc đối chiếu giữa giao thức SOAP và lưu trữ vật lý trong Database.

## Các nội dung chính

1.  **Thông tin kết nối**: [db_connection.md](references/db_connection.md)
    - JDBC URL, Credentials, Dialect.
2.  **Mapping Khách hàng**: [mapping_client.md](references/mapping_client.md)
    - Đối chiếu SOAP API Client -> Bảng `CLIENT`.
3.  **Mapping Hợp đồng**: [mapping_contract.md](references/mapping_contract.md)
    - Đối chiếu SOAP API Contract -> Bảng `ACNT_CONTRACT`.
    - Giải thích về phân cấp hợp đồng (`CONTRACT_LEVEL`).

## Công cụ hỗ trợ

Kỹ năng đi kèm với một script Python để thực thi truy vấn SQL:
- **Script**: `scripts/query_db.py`
- **Cách dùng**: `python scripts/query_db.py "SELECT ..."`
- **Yêu cầu**: Cần cài đặt `pip install oracledb`.

## Hướng dẫn phân tích

Khi nhận được một lỗi hoặc cần kiểm tra dữ liệu:
- Xác định API đang gọi (ví dụ `CreateClientV4`).
- Sử dụng [mapping_client.md](references/mapping_client.md) để biết dữ liệu đó sẽ rơi vào cột nào.
- Sử dụng script `query_db.py` để lấy dữ liệu thực tế từ DB và so sánh với mong đợi.
- Chú ý các trường `AMND_STATE` và `AMND_DATE` để xác định bản ghi mới nhất (Way4 không xóa bản ghi mà tạo version mới).
