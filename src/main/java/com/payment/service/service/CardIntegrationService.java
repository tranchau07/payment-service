package com.payment.service.service;

import com.payment.service.dto.request.CreateCardRequest;
import com.payment.service.dto.response.CreateCardResponse;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardIntegrationService {

    private final SoapPayloadBuilderService payloadBuilderService;
    private final RestTemplate restTemplate;

    @Value("${soap.webservice.url}")
    private String soapEndpoint;

    public CreateCardResponse createCard(CreateCardRequest request) {
        String correlationId = UUID.randomUUID().toString();
        log.info("Bắt đầu quy trình tạo thẻ. CorrelationID: {}", correlationId);

        String xmlPayload = payloadBuilderService.buildCreateCardPayload(request, correlationId);
        String xmlResponse = sendSoapRequest(xmlPayload, correlationId);

        return XmlParserUtil.parseCreateCardResponse(xmlResponse);
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

            log.info("Giao dịch SOAP thành công. CorrelationID: {}. HTTP Status: {}", correlationId, response.getStatusCode());
            return response.getBody();

        } catch (Exception e) {
            log.error("Lỗi khi kết nối SOAP cho CorrelationID: {}. Lỗi: {}", correlationId, e.getMessage());
            throw new RuntimeException("Lỗi tích hợp hệ thống Core Way4: " + e.getMessage(), e);
        }
    }
}
