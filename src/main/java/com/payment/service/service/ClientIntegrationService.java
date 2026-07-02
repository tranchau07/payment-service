package com.payment.service.service;

import com.payment.service.dto.request.CreateClientRequest;
import com.payment.service.dto.request.CreateIssuingContractWithLiabilityRequest;
import com.payment.service.dto.request.CreateMerchantRequest;
import com.payment.service.dto.response.CreateClientResponse;
import com.payment.service.dto.response.CreateContractResponse;
import com.payment.service.dto.response.CreateIssuingContractWithLiabilityResponse;
import com.payment.service.dto.response.CreateMerchantResponse;
import com.payment.service.entity.Client;
import com.payment.service.repository.ClientRepository;
import com.payment.service.util.XmlParserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import com.payment.service.exception.CoreIntegrationException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientIntegrationService {

    private final SoapPayloadBuilderService payloadBuilderService;
    private final RestTemplate restTemplate;
    private final ClientRepository clientRepository;

    @Value("${soap.webservice.url}")
    private String soapEndpoint;

    public CreateClientResponse registerClientToCore(CreateClientRequest request) {
        String correlationId = UUID.randomUUID().toString();
        log.info("Bắt đầu xử lý đăng ký khách hàng. CorrelationID: {}", correlationId);

        // 1. Gửi request tạo Client
        String clientXmlPayload = payloadBuilderService.buildCreateClientPayload(request, correlationId);
        String clientXmlResponse = sendSoapRequest(clientXmlPayload, correlationId);
        
        CreateClientResponse clientResponse = XmlParserUtil.parseCreateClientResponse(clientXmlResponse);
        
        if (!clientResponse.isSuccess()) {
            log.error("Tạo khách hàng thất bại: {}. CorrelationID: {}", clientResponse.getRetMsg(), correlationId);
            throw new com.payment.service.exception.AppException(
                com.payment.service.exception.ErrorCode.CORE_CLIENT_CREATION_FAILED, 
                correlationId, 
                "Tạo khách hàng thất bại: " + clientResponse.getRetMsg(),
                clientResponse.getRetMsg()
            );
        }

        log.info("Tạo khách hàng thành công. NewClientID: {}. CorrelationID: {}", clientResponse.getNewClientId(), correlationId);

        // 2. Cập nhật địa chỉ (nếu có trong request)
        if (request.getAddressInfo() != null) {
            try {
                updateClientAddressViaSoap(request.getAddressInfo(), request.getReason(), String.valueOf(clientResponse.getNewClientId()), correlationId);
                clientResponse.setAddressUpdateStatus("SUCCESS");
            } catch (Exception e) {
                log.error("Lỗi khi cập nhật địa chỉ cho Client ID: {}. Lỗi: {}", clientResponse.getNewClientId(), e.getMessage());
                clientResponse.setAddressUpdateStatus("FAILED: " + e.getMessage());
            }
        } else {
            clientResponse.setAddressUpdateStatus("SKIPPED");
        }

        return clientResponse;
    }

    public void updateClientAddressViaSoap(CreateClientRequest.AddressInfo addressInfo, String reason, String clientId, String correlationId) {
        log.info("Bắt đầu cập nhật địa chỉ qua SOAP cho khách hàng ID: {}. CorrelationID: {}", clientId, correlationId);

        String addressXmlPayload = payloadBuilderService.buildCreateClientAddressPayload(addressInfo, reason, clientId, correlationId);
        String addressXmlResponse = sendSoapRequest(addressXmlPayload, correlationId);

        // Parse response địa chỉ nếu cần validate chi tiết, ở đây tạm thời coi là xong nếu không throw exception
        log.info("Cập nhật địa chỉ SOAP hoàn tất cho ID: {}. CorrelationID: {}", clientId, correlationId);
    }

    public CreateContractResponse createLiabilityContractViaSoap(Long clientId, String branch, String institutionCode, String cbsNumber, String correlationId) {
        String contractXmlPayload = payloadBuilderService.buildCreateContractPayload(clientId, branch, institutionCode, cbsNumber, correlationId);
        String contractXmlResponse = sendSoapRequest(contractXmlPayload, correlationId);
        return XmlParserUtil.parseCreateContractResponse(contractXmlResponse);
    }

    public CreateIssuingContractWithLiabilityResponse createIssuingContractWithLiability(CreateIssuingContractWithLiabilityRequest request) {
        String correlationId = UUID.randomUUID().toString();
        log.info("Bắt đầu tạo hợp đồng Issuing với Liability. CorrelationID: {}", correlationId);

        String xmlPayload = payloadBuilderService.buildCreateIssuingContractWithLiabilityPayload(request, correlationId);
        String xmlResponse = sendSoapRequest(xmlPayload, correlationId);

        CreateIssuingContractWithLiabilityResponse response = XmlParserUtil.parseCreateIssuingContractWithLiabilityResponse(xmlResponse);
        if (!response.isSuccess()) {
            log.error("Tạo hợp đồng Issuing với Liability thất bại: {}. CorrelationID: {}", response.getRetMsg(), correlationId);
            throw new com.payment.service.exception.AppException(
                com.payment.service.exception.ErrorCode.CORE_CONTRACT_CREATION_FAILED, 
                correlationId, 
                "Tạo hợp đồng Issuing thất bại: " + response.getRetMsg(),
                response.getRetMsg()
            );
        }
        return response;
    }

    private String sendSoapRequest(String xmlPayload, String correlationId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);

        HttpEntity<String> httpEntity = new HttpEntity<>(xmlPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    soapEndpoint,
                    httpEntity,
                    String.class
            );

            log.info("Giao dịch thành công. CorrelationID: {}. HTTP Status: {}", correlationId, response.getStatusCode());
            return response.getBody();

        } catch (Exception e) {
            log.error("Lỗi khi kết nối SOAP cho CorrelationID: {}. Lỗi: {}", correlationId, e.getMessage());
            throw new CoreIntegrationException(correlationId, e);
        }
    }

    public CreateMerchantResponse registerMerchantToCore(CreateMerchantRequest request) {
        // 1. Kiểm tra trùng lặp clientNumber trong DB
        List<Client> existingClients = clientRepository.findByClientNumberAndAmndState(request.getClientNumber(), "A");
        if (existingClients != null && !existingClients.isEmpty()) {
            log.warn("Mã khách hàng (Client Number) {} đã tồn tại trên hệ thống.", request.getClientNumber());
            throw new com.payment.service.exception.AppException(
                com.payment.service.exception.ErrorCode.CLIENT_ALREADY_EXISTS,
                null,
                "Mã khách hàng (Client Number) đã tồn tại trong hệ thống: " + request.getClientNumber(),
                "Client Number already exists"
            );
        }

        // 2. Kiểm tra trùng lặp TIN (itn) trong DB
        if (request.getTin() != null && !request.getTin().trim().isEmpty()) {
            List<Client> existingTin = clientRepository.findByItnAndAmndState(request.getTin().trim(), "A");
            if (existingTin != null && !existingTin.isEmpty()) {
                log.warn("Mã số thuế (TIN) {} đã tồn tại trên hệ thống.", request.getTin());
                throw new com.payment.service.exception.AppException(
                    com.payment.service.exception.ErrorCode.CLIENT_ALREADY_EXISTS,
                    null,
                    "Mã số thuế (TIN) đã tồn tại trong hệ thống: " + request.getTin(),
                    "TIN already exists"
                );
            }
        }

        // 3. Kiểm tra trùng lặp registrationNumber (regNumber) trong DB
        if (request.getRegistrationNumber() != null && !request.getRegistrationNumber().trim().isEmpty()) {
            List<Client> existingRegNum = clientRepository.findByRegNumberAndAmndState(request.getRegistrationNumber().trim(), "A");
            if (existingRegNum != null && !existingRegNum.isEmpty()) {
                log.warn("Số đăng ký kinh doanh/giấy tờ (Registration Number) {} đã tồn tại trên hệ thống.", request.getRegistrationNumber());
                throw new com.payment.service.exception.AppException(
                    com.payment.service.exception.ErrorCode.CLIENT_ALREADY_EXISTS,
                    null,
                    "Số đăng ký kinh doanh/giấy tờ đã tồn tại trong hệ thống: " + request.getRegistrationNumber(),
                    "Registration Number already exists"
                );
            }
        }

        String correlationId = UUID.randomUUID().toString();
        log.info("Bắt đầu xử lý đăng ký Merchant. CorrelationID: {}", correlationId);

        // 2. Gửi request tạo Merchant
        String merchantXmlPayload = payloadBuilderService.buildCreateMerchantPayload(request, correlationId);
        String merchantXmlResponse = sendSoapRequest(merchantXmlPayload, correlationId);

        CreateMerchantResponse merchantResponse;
        try {
            merchantResponse = XmlParserUtil.parseCreateMerchantResponse(merchantXmlResponse);
        } catch (RuntimeException ex) {
            throw new CoreIntegrationException(correlationId, ex);
        }

        if (!merchantResponse.isSuccess()) {
            log.error("Tạo Merchant thất bại: {}. CorrelationID: {}", merchantResponse.getRetMsg(), correlationId);
            throw new com.payment.service.exception.AppException(
                com.payment.service.exception.ErrorCode.CORE_CLIENT_CREATION_FAILED, 
                correlationId, 
                "Tạo Merchant thất bại: " + merchantResponse.getRetMsg(),
                merchantResponse.getRetMsg()
            );
        }

        log.info("Tạo Merchant thành công. NewMerchantID: {}. CorrelationID: {}", merchantResponse.getNewMerchantId(), correlationId);

        return merchantResponse;
    }

    public com.payment.service.dto.response.CreateAcquiringContractResponse createAcquiringContractV2(com.payment.service.dto.request.CreateAcquiringContractRequest request) {
        String correlationId = UUID.randomUUID().toString();
        log.info("Bắt đầu tạo hợp đồng Acquiring qua SOAP. CorrelationID: {}", correlationId);

        String xmlPayload = payloadBuilderService.buildCreateAcquiringContractV2Payload(request, correlationId);
        String xmlResponse = sendSoapRequest(xmlPayload, correlationId);

        com.payment.service.dto.response.CreateAcquiringContractResponse response;
        try {
            response = XmlParserUtil.parseCreateAcquiringContractV2Response(xmlResponse);
        } catch (RuntimeException ex) {
            throw new CoreIntegrationException(correlationId, ex);
        }
        if (!response.isSuccess()) {
            log.error("Tạo hợp đồng Acquiring qua SOAP thất bại: {}. CorrelationID: {}", response.getRetMsg(), correlationId);
            throw new com.payment.service.exception.AppException(
                com.payment.service.exception.ErrorCode.CORE_CONTRACT_CREATION_FAILED,
                correlationId,
                "Tạo hợp đồng Acquiring thất bại: " + response.getRetMsg(),
                response.getRetMsg()
            );
        }
        return response;
    }

    public com.payment.service.dto.response.CreateAcquiringContractAddressResponse createAcquiringContractAddress(String contractNumber, com.payment.service.dto.request.CreateAcquiringContractAddressRequest address, String correlationId) {
        log.info("Bắt đầu tạo địa chỉ hợp đồng Acquiring qua SOAP. CorrelationID: {}", correlationId);
        String xmlPayload = payloadBuilderService.buildCreateAcquiringContractAddressPayload(contractNumber, address, correlationId);
        String xmlResponse = sendSoapRequest(xmlPayload, correlationId);

        com.payment.service.dto.response.CreateAcquiringContractAddressResponse response;
        try {
            response = XmlParserUtil.parseCreateAcquiringContractAddressResponse(xmlResponse);
        } catch (RuntimeException ex) {
            throw new CoreIntegrationException(correlationId, ex);
        }
        if (!response.isSuccess()) {
            log.error("Tạo địa chỉ hợp đồng Acquiring thất bại: {}. CorrelationID: {}", response.getRetMsg(), correlationId);
            throw new com.payment.service.exception.AppException(
                com.payment.service.exception.ErrorCode.CORE_CONTRACT_CREATION_FAILED,
                correlationId,
                "Tạo địa chỉ hợp đồng Acquiring thất bại: " + response.getRetMsg(),
                response.getRetMsg()
            );
        }
        return response;
    }

    public com.payment.service.dto.response.CreateDeviceResponse createDevice(com.payment.service.dto.request.CreateDeviceRequest request) {
        String correlationId = UUID.randomUUID().toString();
        log.info("Bắt đầu tạo Device qua SOAP. CorrelationID: {}", correlationId);

        String xmlPayload = payloadBuilderService.buildCreateDevicePayload(request, correlationId);
        String xmlResponse = sendSoapRequest(xmlPayload, correlationId);

        com.payment.service.dto.response.CreateDeviceResponse response;
        try {
            response = XmlParserUtil.parseCreateDeviceResponse(xmlResponse);
        } catch (RuntimeException ex) {
            throw new CoreIntegrationException(correlationId, ex);
        }
        if (!response.isSuccess()) {
            log.error("Tạo Device qua SOAP thất bại: {}. CorrelationID: {}", response.getRetMsg(), correlationId);
            throw new com.payment.service.exception.AppException(
                com.payment.service.exception.ErrorCode.CORE_CONTRACT_CREATION_FAILED,
                correlationId,
                "Tạo Device thất bại: " + response.getRetMsg(),
                response.getRetMsg()
            );
        }
        return response;
    }
}
