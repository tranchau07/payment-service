package com.payment.service.service;

import com.payment.service.dto.request.DocSearchRequest;
import com.payment.service.dto.response.DocResponse;
import com.payment.service.dto.response.ContractCashFlowResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DocServiceIntegrationTest {

    @Autowired
    DocService docService;

    @BeforeEach
    void authenticateAsTeller() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "doc-test-teller",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_TELLER"))));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void enrichesDocumentsFromDatabaseCatalogAndMasksSensitiveValuesForTeller() {
        Page<DocResponse> page = docService.searchDocs(DocSearchRequest.builder()
                .page(0)
                .size(100)
                .sort("transDate,desc")
                .build());

        assertFalse(page.isEmpty());
        assertTrue(page.stream().anyMatch(doc -> doc.getPostingStatus() != null));
        assertTrue(page.stream()
                .filter(doc -> doc.getTransType() != null)
                .allMatch(doc -> doc.getTransTypeName() != null));
        assertTrue(page.stream()
                .map(DocResponse::getTargetNumber)
                .filter(value -> value != null && value.length() > 4)
                .anyMatch(value -> value.contains("***")));
    }

    @Test
    void exposesFilterMetadataFromWay4Catalog() {
        Map<String, Object> metadata = docService.getFilterMetadata();

        assertFalse(((List<?>) metadata.get("transactionTypes")).isEmpty());
        assertTrue(((List<?>) metadata.get("postingStatuses")).contains("J"));
        assertTrue(((List<?>) metadata.get("documentCategories")).contains("Y"));
    }

    @Test
    void summarizesPostedFinancialDocumentsByContract() {
        List<ContractCashFlowResponse> cashFlows = docService.getContractCashFlows();

        assertFalse(cashFlows.isEmpty());
        assertTrue(cashFlows.stream().allMatch(row -> row.getContractId() != null));
        assertTrue(cashFlows.stream().allMatch(row -> row.getTransactionCount() > 0));
        assertTrue(cashFlows.stream().allMatch(row ->
                row.getNetCashFlow().compareTo(row.getTotalReceived().subtract(row.getTotalSent())) == 0));
    }
}
