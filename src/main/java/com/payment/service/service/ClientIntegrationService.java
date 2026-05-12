package com.payment.service.service;

import com.payment.service.dto.request.CreateClientRequest;
import com.payment.service.dto.response.CreateClientResponse;
import com.payment.service.util.XmlParserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientIntegrationService {

    private final SoapPayloadBuilderService payloadBuilderService;
    private final RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${soap.webservice.url}")
    private String soapEndpoint;

    @Transactional(rollbackFor = Exception.class)
    public CreateClientResponse registerClientToCore(CreateClientRequest request) {
        String correlationId = UUID.randomUUID().toString();
        log.info("Bắt đầu xử lý đăng ký khách hàng. CorrelationID: {}", correlationId);

        // 1. Gửi request tạo Client
        String clientXmlPayload = payloadBuilderService.buildCreateClientPayload(request, correlationId);
        String clientXmlResponse = sendSoapRequest(clientXmlPayload, correlationId);
        
        CreateClientResponse clientResponse = XmlParserUtil.parseCreateClientResponse(clientXmlResponse);
        
        if (!clientResponse.isSuccess()) {
            log.error("Tạo khách hàng thất bại: {}. CorrelationID: {}", clientResponse.getRetMsg(), correlationId);
            return clientResponse;
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
            throw new RuntimeException("Lỗi tích hợp hệ thống Core: " + e.getMessage(), e);
        }
    }
}
