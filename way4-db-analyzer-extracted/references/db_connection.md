# Way4 Database Connection

Thông tin kết nối cơ sở dữ liệu Way4 từ cấu hình ứng dụng.

## Datasource Configuration
- **JDBC URL**: `jdbc:oracle:thin:@10.145.48.96:1521:way4db`
- **Username**: `INT`
- **Password**: `way4`
- **Driver**: `oracle.jdbc.OracleDriver`
- **Dialect**: `org.hibernate.dialect.OracleDialect`

## Database Environment
- **Type**: Oracle Database
- **Host**: `10.145.48.96`
- **Port**: `1521`
- **SID/Service Name**: `way4db`

## Note
Cơ sở dữ liệu này chứa các bảng core của hệ thống Way4 như `CLIENT`, `ACNT_CONTRACT`, `ADDRESS_TYPE`, v.v. Các bảng này có cơ chế lưu vết thay đổi (versioning) thông qua các trường `AMND_STATE`, `AMND_DATE`.
