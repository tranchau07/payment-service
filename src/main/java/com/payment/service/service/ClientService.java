package com.payment.service.service;

import com.payment.service.dto.request.ClientSearchRequest;
import com.payment.service.dto.request.CreateClientRequest;
import com.payment.service.dto.response.ClientListResponse;
import com.payment.service.entity.Client;
import com.payment.service.mapper.ClientMapper;
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

    @Transactional(readOnly = true)
    public ClientListResponse searchClients(ClientSearchRequest searchRequest, Pageable pageable) {
        log.info("Searching clients with criteria: {}, pageable: {}", searchRequest, pageable);

        Specification<Client> spec = ClientSpecification.buildSearch(searchRequest);
        Page<Client> clientPage = clientRepository.findAll(spec, pageable);

        log.info("Found {} clients", clientPage.getTotalElements());
        return clientMapper.toListResponse(clientPage);
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

        Client client = clientOpt.get();
        if (addressInfo != null) {
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
        return false;
    }

}
