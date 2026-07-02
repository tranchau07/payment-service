package com.payment.service.service;

import com.payment.service.dto.response.ProductConfigurationResponse;
import com.payment.service.entity.*;
import com.payment.service.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductConfigurationServiceTest {
    @Mock ApplProductRepository productRepository;
    @Mock ContractTypeRepository contractTypeRepository;
    @Mock ContractSubtypeRepository contractSubtypeRepository;
    @Mock AccountSchemeRepository accountSchemeRepository;
    @Mock ServicePackRepository servicePackRepository;
    @InjectMocks ProductConfigurationService service;

    @Test
    void issuingEndpointUsesPcatCAndResolvesVisualReferences() {
        ApplProduct product = product("CARD_GOLD", "C");
        ContractType contractType = mock(ContractType.class);
        when(contractType.getId()).thenReturn(11L);
        when(contractType.getCode()).thenReturn("CARD");
        when(contractType.getName()).thenReturn("Card contract");
        AccountScheme accountScheme = mock(AccountScheme.class);
        when(accountScheme.getId()).thenReturn(13L);
        when(accountScheme.getCode()).thenReturn("VND_CARD");
        when(accountScheme.getName()).thenReturn("VND card account");

        when(productRepository.findAllByPcatAndAmndStateAndIsReadyOrderByNameAsc("C", "A", "Y"))
                .thenReturn(List.of(product));
        when(contractTypeRepository.findAllByIdInAndAmndState(anyCollection(), eq("A")))
                .thenReturn(List.of(contractType));
        when(accountSchemeRepository.findAllByIdInAndAmndState(anyCollection(), eq("A")))
                .thenReturn(List.of(accountScheme));

        ProductConfigurationResponse response = service
                .getProducts(ProductConfigurationService.BusinessType.ISSUING, false).getFirst();

        assertEquals("ISSUING", response.getBusinessType());
        assertEquals("CARD - Card contract", response.getContractType().getDisplayName());
        assertEquals("ACC_SCHEME", response.getAccountScheme().getSourceTable());
        assertTrue(response.getMissingReferences().contains("CONTR_SUBTYPE:12"));
        assertTrue(response.getMissingReferences().contains("SERV_PACK:14"));
    }

    @Test
    void acquiringEndpointUsesPcatM() {
        when(productRepository.findAllByPcatAndAmndStateAndIsReadyOrderByNameAsc("M", "A", "Y"))
                .thenReturn(List.of(product("RETAIL_ACC", "M")));

        ProductConfigurationResponse response = service
                .getProducts(ProductConfigurationService.BusinessType.ACQUIRING, false).getFirst();

        assertEquals("ACQUIRING", response.getBusinessType());
        assertEquals("M", response.getPcat());
        verify(productRepository).findAllByPcatAndAmndStateAndIsReadyOrderByNameAsc("M", "A", "Y");
    }

    private static ApplProduct product(String code, String pcat) {
        ApplProduct product = ApplProduct.builder()
                .id(1L).code(code).name(code).internalCode("161103000000000000000001")
                .pcat(pcat).conCat("C").contrType(11L).contrSubtype(12L)
                .accScheme(13L).servicePack(14L).isActive("Y").isReady("Y").build();
        product.setAmndState("A");
        return product;
    }
}
