package com.payment.service.service;

import com.payment.service.dto.request.CreateAcquiringContractRequest;
import com.payment.service.dto.response.CreateAcquiringContractResponse;
import com.payment.service.entity.ApplProduct;
import com.payment.service.entity.Client;
import com.payment.service.entity.Currency;
import com.payment.service.entity.Sic;
import com.payment.service.mapper.ContractMapper;
import com.payment.service.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {
    @Mock AcntContractRepository contractRepository;
    @Mock ClientRepository clientRepository;
    @Mock ContractMapper contractMapper;
    @Mock ClientIntegrationService integrationService;
    @Mock ApplProductRepository productRepository;
    @Mock SicRepository sicRepository;
    @Mock CurrencyRepository currencyRepository;
    @InjectMocks ContractService service;

    @Test
    void sendsNumericCurrencyCodeToWay4() {
        Client merchant = new Client();
        merchant.setAmndState("A");
        merchant.setProductCategory("M");
        ApplProduct product = ApplProduct.builder().code("RETAIL_ACC").conCat("M").isActive("Y").build();
        product.setAmndState("A");
        Sic sic = new Sic();
        sic.setUseInBank("Y");
        Currency currency = Currency.builder().code("RUB").numericCode("810").isActive("Y").build();
        CreateAcquiringContractResponse expected = CreateAcquiringContractResponse.builder().retCode(0L).build();

        when(clientRepository.findById(10L)).thenReturn(Optional.of(merchant));
        when(productRepository.findByCode("RETAIL_ACC")).thenReturn(Optional.of(product));
        when(sicRepository.findByCodeAndAmndState("5411", "A")).thenReturn(Optional.of(sic));
        when(currencyRepository.findByNumericCode("810")).thenReturn(Optional.of(currency));
        when(integrationService.createAcquiringContractV2(any())).thenReturn(expected);

        CreateAcquiringContractRequest request = CreateAcquiringContractRequest.builder()
                .clientSearchMethod("CLIENT_ID").clientIdentifier("10").productCode("RETAIL_ACC")
                .inObject(CreateAcquiringContractRequest.InObject.builder()
                        .institutionCode("0001").branch("0101").contractName("Merchant Contract")
                        .currency("810").mcc("5411").merchantId("MID001").cbsNumber("CBS001")
                        .openDate("2026-06-30").build())
                .build();

        service.createAcquiringContractV2(request);
        assertEquals("810", request.getInObject().getCurrency());
        verify(integrationService).createAcquiringContractV2(request);
    }
}
