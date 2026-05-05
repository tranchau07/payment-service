package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientListResponse {

    List<ClientResponse> data;

    Integer currentPage;           // Trang hiện tại (thường bắt đầu từ 0 hoặc 1)
    Integer pageSize;              // Số record trên 1 trang
    Long totalElements;            // Tổng số record trong DB
    Integer totalPages;            // Tổng số trang
    Boolean hasNext;               // Cờ kiểm tra xem còn trang tiếp theo không

    Long retCode;                  // 0 = success
    String retMsg;

    String debugInfo;
    String resultInfo;

    public boolean isSuccess() {
        return retCode != null && retCode == 0L;
    }
}