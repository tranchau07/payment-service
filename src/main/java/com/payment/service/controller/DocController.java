package com.payment.service.controller;

import com.payment.service.dto.request.DocSearchRequest;
import com.payment.service.dto.response.DocResponse;
import com.payment.service.dto.response.ContractCashFlowResponse;
import com.payment.service.service.DocService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/docs")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PreAuthorize("hasAnyRole('TELLER', 'SUPERVISOR', 'ADMIN')")
public class DocController {

    DocService docService;

    @GetMapping("/metadata")
    public ResponseEntity<Map<String, Object>> getFilterMetadata() {
        return ResponseEntity.ok(docService.getFilterMetadata());
    }

    @GetMapping("/contracts/cash-flow")
    public ResponseEntity<List<ContractCashFlowResponse>> getContractCashFlows() {
        return ResponseEntity.ok(docService.getContractCashFlows());
    }

    @GetMapping
    public ResponseEntity<Page<DocResponse>> searchDocs(@ModelAttribute DocSearchRequest request) {
        return ResponseEntity.ok(docService.searchDocs(request));
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<Page<DocResponse>> getDocsByContract(
            @PathVariable Long contractId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(docService.getDocsByContract(contractId, page, size));
    }
}
