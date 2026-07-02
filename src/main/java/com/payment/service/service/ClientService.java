package com.payment.service.service;

import com.payment.service.dto.request.ClientSearchRequest;
import com.payment.service.dto.request.CreateClientRequest;
import com.payment.service.dto.response.ClientListResponse;
import com.payment.service.dto.response.ClientResponse;
import com.payment.service.entity.Client;
import com.payment.service.mapper.ClientMapper;
import com.payment.service.repository.AcntContractRepository;
import com.payment.service.repository.ApplProductRepository;
import com.payment.service.repository.ClientRepository;
import com.payment.service.repository.specification.ClientSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientService {

    ClientRepository clientRepository;
    ClientMapper clientMapper;
    AcntContractRepository contractRepository;
    ApplProductRepository productRepository;

    @Transactional(readOnly = true)
    public ClientListResponse searchClients(ClientSearchRequest searchRequest, Pageable pageable) {
        log.info("Searching clients with criteria: {}, pageable: {}", searchRequest, pageable);

        Specification<Client> spec = ClientSpecification.buildSearch(searchRequest);
        Page<Client> clientPage = clientRepository.findAll(spec, pageable);

        log.info("Found {} clients", clientPage.getTotalElements());
        return clientMapper.toListResponse(clientPage);
    }

    @Transactional(readOnly = true)
    public ClientResponse getClientById(Long id) {
        log.info("Fetching client with ID: {}", id);

        return clientRepository.findById(id)
                .map(clientMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateClientAddress(Long clientId, CreateClientRequest.AddressInfo addressInfo) {
        log.info("Updating address for client ID: {}", clientId);

        Optional<Client> clientOpt = clientRepository.findById(clientId);
        log.info(clientOpt.toString());

        if (clientOpt.isEmpty()) {
            log.warn("Client not found with ID: {}. Skipping address update.", clientId);
            return false;
        }

        if (addressInfo == null) {
            log.warn("Address info is null. Skipping address update for client ID: {}", clientId);
            return false;
        }

        Client client = clientOpt.get();

        client.setCountry(addressInfo.getCountry());
        client.setAddressZip(addressInfo.getAddressZip());
        client.setState(addressInfo.getState());
        client.setCity(addressInfo.getCity());
        client.setAddressLine1(addressInfo.getAddressLine1());
        client.setAddressLine2(addressInfo.getAddressLine2());
        client.setAddressLine3(addressInfo.getAddressLine3());
        client.setAddressLine4(addressInfo.getAddressLine4());

        clientRepository.save(client);

        log.info("Address updated successfully for client ID: {}", clientId);
        return true;
    }

    @Transactional(readOnly = true)
    public com.payment.service.dto.response.ClientHierarchyResponse getClientHierarchy(Long clientId) {
        log.info("Xây dựng cây phả hệ cho client ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new com.payment.service.exception.AppException(
                        com.payment.service.exception.ErrorCode.CLIENT_NOT_FOUND,
                        null,
                        "Không tìm thấy khách hàng với ID: " + clientId,
                        null
                ));

        ClientResponse clientResponse = clientMapper.toResponse(client);

        java.util.List<com.payment.service.entity.AcntContract> contracts =
                contractRepository.findByClientIdAndAmndState(clientId, "A");

        java.util.List<com.payment.service.entity.ApplProduct> products =
                productRepository.findAll();

        java.util.Map<Long, com.payment.service.entity.ApplProduct> productMap = products.stream()
                .collect(java.util.stream.Collectors.toMap(
                        com.payment.service.entity.ApplProduct::getId,
                        java.util.function.Function.identity(),
                        (p1, p2) -> p1
                ));

        java.util.List<com.payment.service.dto.response.ContractNodeResponse> nodes = contracts.stream()
                .map(c -> {
                    String productCode = null;
                    String productName = null;
                    String productType = "UNKNOWN";

                    String productStr = c.getProduct();

                    if (productStr != null && productStr.length() > 6) {
                        try {
                            long parsedId = Long.parseLong(productStr.substring(6));
                            com.payment.service.entity.ApplProduct p = productMap.get(parsedId);

                            if (p != null) {
                                productCode = p.getCode();
                                productName = p.getName();
                            }
                        } catch (NumberFormatException e) {
                            log.warn("Invalid product ID format in contract: {}", productStr);
                        }
                    }

                    if ("LIAB_TRAINING01".equals(productCode)) {
                        productType = "LIABILITY";
                    } else if ("ISSUING_TRAINING01".equals(productCode)) {
                        productType = "ISSUING";
                    } else if ("C".equalsIgnoreCase(c.getConCat())) {
                        productType = "CARD";
                    } else if ("A".equalsIgnoreCase(c.getConCat()) && "M".equalsIgnoreCase(c.getPcat())) {
                        productType = "ACQUIRING";
                    } else if ("M".equalsIgnoreCase(c.getConCat()) && "M".equalsIgnoreCase(c.getPcat())) {
                        productType = "DEVICE";
                    } else if ("M".equalsIgnoreCase(c.getConCat())) {
                        productType = "ACQUIRING";
                    } else if ("T".equalsIgnoreCase(c.getConCat())) {
                        productType = "DEVICE";
                    }

                    Long parentId = null;

                    if ("CARD".equals(productType)) {
                        parentId = c.getAcntContractOid() != null
                                ? c.getAcntContractOid()
                                : c.getBillingContract();
                    } else if ("ISSUING".equals(productType)) {
                        parentId = c.getLiabContract();
                    } else if ("DEVICE".equals(productType)) {
                        parentId = c.getAcntContractOid() != null
                                ? c.getAcntContractOid()
                                : c.getBillingContract();
                    }

                    String addressLine1 = null;
                    String city = null;
                    String country = null;

                    if ("ACQUIRING".equals(productType)) {
                        Optional<AcntContractRepository.ContractAddressProjection> addrOpt =
                                contractRepository.findContractAddressFields(c.getId());

                        if (addrOpt.isPresent()) {
                            AcntContractRepository.ContractAddressProjection addr = addrOpt.get();

                            addressLine1 = addr.getAddressLine1();
                            city = addr.getCity();
                            country = addr.getCountry();
                        }
                    }

                    return com.payment.service.dto.response.ContractNodeResponse.builder()
                            .id(c.getId())
                            .contractNumber(c.getContractNumber())
                            .contractName(c.getContractName())
                            .contractLevel(c.getContractLevel())
                            .productCode(productCode)
                            .productName(productName)
                            .productType(productType)
                            .parentId(parentId)
                            .liabContract(c.getLiabContract())
                            .acntContractOid(c.getAcntContractOid())
                            .billingContract(c.getBillingContract())
                            .curr(c.getCurr())
                            .amountAvailable(c.getAmountAvailable())
                            .totalBalance(c.getTotalBalance())
                            .dateOpen(c.getDateOpen())
                            .dateExpire(c.getDateExpire())
                            .addressLine1(addressLine1)
                            .city(city)
                            .country(country)
                            .build();
                })
                .collect(java.util.stream.Collectors.toList());

        return com.payment.service.dto.response.ClientHierarchyResponse.builder()
                .client(clientResponse)
                .contracts(nodes)
                .build();
    }
}