# Tài Liệu Đặc Tả Yêu Cầu Nghiệp Vụ (PRD) - Hệ Thống Phát Hành Thẻ (Core Way4)

**Vai trò:** Senior Product Manager
**Đối tượng đọc:** Đội ngũ phát triển Frontend (Web/App)
**Mục tiêu:** Cung cấp đặc tả chi tiết về luồng nghiệp vụ, giao diện, và cách thức giao tiếp (Data Mapping) với hệ thống Backend (Core Banking Way4) cho quy trình Phát hành thẻ. Đảm bảo Frontend dev có thể đọc, hiểu và implement được ngay mà không cần họp lại để xác nhận field.

---

## 1. TỔNG QUAN HỆ THỐNG & LUỒNG NGHIỆP VỤ (BUSINESS FLOW)

Hệ thống Phát hành thẻ trên nền tảng Way4 được chia thành **3 giai đoạn chính**, bắt buộc phải thực hiện tuần tự để phát hành được một chiếc thẻ vật lý/phi vật lý cho Khách hàng:

1. **Giai đoạn 1: Mở CIF (Customer Information File)**
   - Hệ thống tạo hồ sơ Khách hàng (Client), lưu trữ thông tin nhân khẩu học và địa chỉ.
   - *Lưu ý từ Backend:* Hiện tại API Đăng ký Client đang tự động thực hiện 3 bước: Tạo CIF -> Cập nhật địa chỉ -> Tự động mở Hợp đồng mặc định (Contract). Frontend cần xử lý UI để thể hiện tiến trình này cho user (Teller/Giao dịch viên).
2. **Giai đoạn 2: Mở Hợp đồng phát hành (Issuing Contract)**
   - Gắn kết Khách hàng với một Hợp đồng tài khoản (ví dụ: Tài khoản thanh toán, Thẻ tín dụng) và thiết lập hạn mức/nghĩa vụ nợ (Liability).
3. **Giai đoạn 3: Phát hành thẻ (Card Issuance)**
   - Dựa trên Hợp đồng đã tạo, tiến hành cá thể hóa thẻ (Dập nổi tên, gán vào Hợp đồng).

---

## 2. CHI TIẾT CÁC MÀN HÌNH (SCREENS) VÀ MAPPING DỮ LIỆU

### Màn hình 1: Quản lý & Tìm kiếm Khách hàng (Dashboard / Client List)
- **Mục đích:** Giao dịch viên tìm kiếm xem khách hàng đã có CIF hay chưa. Nếu chưa có thì tiến hành Mở mới.
- **API sử dụng:** `GET /clients` (Tìm kiếm có phân trang)
- **UI Components:** Ô tìm kiếm, Bảng danh sách kết quả, Nút "Mở CIF Mới".

### Màn hình 2: Đăng ký Khách Hàng (Mở CIF mới)
- **Mục đích:** Thu thập thông tin cá nhân và địa chỉ để mở CIF trên Core Way4.
- **API sử dụng:** `POST /clients/register`
- *(Xem chi tiết mapping field ở các phiên bản trước)*

---

### Màn hình 3: Mở Hợp Đồng Phát Hành (Create Issuing Contract With Liability)
- **Mục đích:** Gắn một Hợp đồng (Contract) và Nghĩa vụ nợ (Liability) cho Khách hàng đã có CIF. Đây là bước bắt buộc trước khi phát hành thẻ.
- **API sử dụng:** `POST /contracts/create-with-liability`
- **UI Logic:** Màn hình này thường được gọi từ nút "Tạo Hợp Đồng" trong trang Chi tiết Khách hàng. Các thông tin về Khách hàng (`clientIdentifier`) phải được pass ẩn từ trang trước sang.

#### Bảng Mapping Giao Diện (UI) -> JSON Payload

| Tên Field trên UI (Giao diện) | Loại Input | Bắt buộc | Field trong JSON | Hướng dẫn / Quy tắc Validate (Frontend cần code) |
| :--- | :--- | :--- | :--- | :--- |
| **ID Khách hàng** | Readonly | **Có** | `clientIdentifier` | Tự động lấy từ URL hoặc State của màn hình trước. |
| **ID Hợp đồng bảo lãnh** | Text | Không | `liabContractIdentifier`| Số Hợp đồng Liability cha. Bắt buộc nếu là thẻ Tín dụng (Credit Card). Ẩn đi nếu là thẻ Ghi nợ (Debit). |
| **Sản phẩm chính** | Dropdown | **Có** | `productCode` | Mã BIN/Loại Thẻ chính (Vd: VCB_VISA_PLATINUM). Lấy từ API Danh mục. |
| **Sản phẩm phụ 1** | Dropdown | Không | `productCode2` | Dịch vụ đi kèm (Vd: SMS Banking). |
| **Sản phẩm phụ 2** | Dropdown | Không | `productCode3` | Dịch vụ đi kèm (Vd: Internet Banking). |
| **Tên Hợp đồng** | Text | **Có** | `contractName` | Tự động sinh trên UI: `[Tên viết tắt Sản phẩm] - [Tên KH]`. Cho phép User sửa. |
| **Tài khoản CBS** | Text | **Có** | `cbsNumber` | Số tài khoản thanh toán Core Banking (CASA). Dùng để trích nợ. Validate: Phải là chuỗi số. |
| **Chi nhánh quản lý** | Dropdown | **Có** | `branch` | Chi nhánh quản lý hợp đồng này. Lấy từ thông tin User đang đăng nhập. |
| **Tổ chức phát hành** | Dropdown | **Có** | `institutionCode` | Mã ngân hàng/Tổ chức (Thường fix cứng hoặc lấy theo chi nhánh). |
| **Thông tin thêm 1** | Text | Không | `addInfo01` | Mã CBNV tiếp thị (Mã giới thiệu). |
| **Thông tin thêm 2** | Text | Không | `addInfo02` | Ghi chú bổ sung (Vd: "KH VIP"). |

#### 📝 Ví dụ Payload JSON (Frontend gửi lên)
```json
{
  "clientIdentifier": "100056789",
  "liabContractIdentifier": "LIAB-998877",
  "productCode": "VISA_DEBIT_STD",
  "productCode2": "FEE_SMS_MONTHLY",
  "productCode3": "",
  "branch": "HCM_BRANCH_01",
  "institutionCode": "MYBANK",
  "contractName": "VISA DEBIT - NGUYEN VAN A",
  "cbsNumber": "19034567890011",
  "addInfo01": "EMP12345",
  "addInfo02": "Thu phi phat hanh the"
}
```
**Xử lý UI sau khi gọi API thành công:**
- Lưu lại `Contract Number` trả về.
- Hiển thị Popup: "Tạo Hợp đồng thành công".
- Chuyển hướng (Redirect) ngay sang **Màn hình 4: Phát hành Thẻ** và tự động fill `Contract Number` vào màn hình đó.

---

### Màn hình 4: Cá thể hóa và Phát hành Thẻ (Create Card)
- **Mục đích:** Từ Hợp đồng (Contract) đã có, xuất ra một thẻ vật lý/phi vật lý. Cung cấp các thông tin sẽ được dập nổi (emboss) lên mặt thẻ nhựa.
- **API sử dụng:** `POST /cards`
- **UI Logic:** Gọi từ trang Chi tiết Hợp đồng hoặc tự động chuyển từ Màn hình 3 sang.

#### Bảng Mapping Giao Diện (UI) -> JSON Payload

| Tên Field trên UI (Giao diện) | Loại Input | Bắt buộc | Field trong JSON | Hướng dẫn / Quy tắc Validate (Frontend CẦN LÀM KỸ) |
| :--- | :--- | :--- | :--- | :--- |
| **Mã Hợp Đồng** | Readonly | **Có** | `contractIdentifier`| Tự động lấy từ Màn hình 3. (Nằm ở root level của JSON). |
| **Mã Sản phẩm Thẻ** | Readonly | **Có** | `productCode` | Tự động lấy từ Màn hình 3 (Nằm ở root level của JSON). |
| **Tên thẻ (Nội bộ)** | Text | **Có** | `inObject.cardName` | Tên thẻ quản lý nội bộ hệ thống. |
| **Tên Dập Nổi (First)**| Text | **Có** | `inObject.embossedFirstName`| **QUAN TRỌNG:** Chỉ cho phép nhập chữ cái A-Z và khoảng trắng (Không số, không ký tự đặc biệt). Tự động `.toUpperCase()`. Giới hạn độ dài: Max 20 ký tự. |
| **Tên Dập Nổi (Last)** | Text | **Có** | `inObject.embossedLastName` | Cắt từ Họ KH. Tương tự như `embossedFirstName`. |
| **Tên Công Ty Dập Nổi**| Text | Không | `inObject.embossedCompanyName`| Dành cho thẻ Doanh nghiệp (Corporate Card). Ẩn field này nếu loại KH là Cá nhân. |
| **Số TK Liên Kết** | Readonly | **Có** | `inObject.cbsNumber` | Số tài khoản CASA. Tự động lấy từ Hợp đồng ở Màn hình 3. |
| **Chi nhánh nhận thẻ** | Dropdown | **Có** | `inObject.branch` | Nơi KH sẽ đến quầy để nhận thẻ cứng. |

#### 📝 Ví dụ Payload JSON (Frontend gửi lên)
```json
{
  "contractIdentifier": "CON-100056789-01",
  "productCode": "VISA_DEBIT_STD",
  "inObject": {
    "cardName": "The ghi no quoc te Visa",
    "branch": "HN_BRANCH_02",
    "cbsNumber": "19034567890011",
    "embossedFirstName": "NGUYEN VAN",
    "embossedLastName": "A",
    "embossedCompanyName": ""
  }
}
```

**Xử lý UI sau khi gọi API thành công:**
- Chuyển trạng thái thẻ trên màn hình thành "Đang chờ in ấn" (Pending Emboss).
- Quay về màn hình Chi tiết Khách hàng.

---

## 3. CÁC LƯU Ý KỸ THUẬT (TECHNICAL NOTES) CHO FRONTEND DEVS

1. **Xử lý Lỗi (Error Handling - HTTP 400 & 422):**
   - Backend sử dụng `GlobalExceptionHandler`. 
   - Lỗi Validation Form (thiếu trường, sai định dạng) sẽ trả về **HTTP 400 Bad Request** với thân body là một Object map: `{"cbsNumber": "must not be blank"}`. Frontend cần bắt mã 400, lặp qua các key này và bôi đỏ (highlight) đúng các input field tương ứng trên Form.
   - Lỗi nghiệp vụ (Business Error) từ Core Way4 (ví dụ: "Client không tồn tại", "Hợp đồng đã có thẻ") sẽ trả về HTTP 400/500 nhưng kèm thông báo lỗi trong object `retMsg` (`success: false`). Phải show Popup Alert cho user đọc.
2. **Quản lý Loading State (Chống Double-Click):**
   Giao dịch với hệ thống Core Way4 (thông qua SOAP) tốn nhiều thời gian (từ 2-5 giây). 
   - **BẮT BUỘC:** Khi user click "Lưu / Tạo Hợp Đồng / Phát Hành", lập tức khóa (Disable) nút bấm và hiển thị Loading Spinner mờ toàn màn hình. Tuyệt đối không để user click 2 lần dẫn đến sinh ra 2 thẻ rác.
3. **Tiền xử lý chuỗi (Data Formatting):**
   - Mọi field liên quan đến "Tên dập nổi" (`embossedFirstName`, `embossedLastName`) phải được thiết lập mask/filter ngay trên lúc gõ: Ép viết hoa (Uppercase) và tự động xóa dấu tiếng Việt (Remove accents). Máy dập thẻ vật lý của ngân hàng **sẽ báo lỗi** nếu có dấu.

---
*Nếu team Frontend có bất kỳ câu hỏi nào về API hoặc thiếu field cho UI thiết kế, vui lòng tag PM vào Jira task để xử lý ngay.*