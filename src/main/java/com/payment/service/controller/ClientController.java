package com.payment.service.controller;

import com.payment.service.dto.request.ClientSearchRequest;
import com.payment.service.dto.request.CreateClientRequest;
import com.payment.service.dto.response.ClientListResponse;
import com.payment.service.dto.response.ClientResponse;
import com.payment.service.dto.response.CreateClientResponse;
import com.payment.service.service.ClientIntegrationService;
import com.payment.service.service.ClientService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PreAuthorize("hasAnyRole('TELLER', 'SUPERVISOR', 'ADMIN')")
public class ClientController {


    ClientIntegrationService clientIntegrationService;
    ClientService clientService;

    @PostMapping("/register")
    public CreateClientResponse registerClient(@jakarta.validation.Valid @RequestBody CreateClientRequest request) {
        log.info("Request to register client: {}", request);
        return clientIntegrationService.registerClientToCore(request);
    }

    @GetMapping
    public ResponseEntity<ClientListResponse> searchClients(
            ClientSearchRequest searchRequest,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("Received request to search clients with criteria: {}", searchRequest);

        ClientListResponse response = clientService.searchClients(searchRequest, pageable);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        log.info("Received request to find client with ID: {}", id);
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PutMapping("/{id}/address")
    public String updateClientAddress(
            @PathVariable Long id,
            @RequestBody CreateClientRequest.AddressInfo addressInfo) {

        String correlationId = java.util.UUID.randomUUID().toString();
        clientIntegrationService.updateClientAddressViaSoap(addressInfo, "Manual address update", String.valueOf(id), correlationId);
        return "Address update requested via SOAP successfully";
    }
}
