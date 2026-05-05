package com.payment.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResponse {

    Long id;
    String clientNumber;

    String shortName;
    String firstName;
    String lastName;
    String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate;

    String email;
    String mobilePhone;

    String country;
    String city;
    String addressLine1;

    // Đã che giấu dữ liệu nhạy cảm
    String maskedSocialNumber;
    String maskedItn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dateOpen;
}