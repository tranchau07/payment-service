package com.payment.service.dto.response;

import com.payment.service.entity.Lang;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountryResponse { String name;
    String code;        // ISO alpha-3 (VNM, USA)
    String code2;       // ISO alpha-2 (VN, US)
    String numericCode; // ISO numeric (704, 840)
    String currencyCode;
    String currencyName;
    String customCode;
    String postalCode;
    String useInBank;
    Long defaultLanguageId;
    Long areaDefault;
    String limitCode;
    Long countryObjectId;
    Long calendarType;
    String numericCurrencyCode;
}
