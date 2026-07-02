package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocSearchRequest {
    Long contractId;
    String number;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Date startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Date endDate;
    Long transType;
    String postingStatus;
    String documentCategory;
    String requestCategory;
    String serviceClass;
    Integer returnCode;
    Integer page;
    Integer size;
    String sort;
}
