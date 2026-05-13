package com.payment.service.service;

import com.payment.service.dto.request.CreateClientRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class SoapPayloadBuilderService {

    @Qualifier("xmlTemplateEngine")
    private final TemplateEngine xmlTemplateEngine;

    public SoapPayloadBuilderService(@Qualifier("xmlTemplateEngine") TemplateEngine xmlTemplateEngine) {
        this.xmlTemplateEngine = xmlTemplateEngine;
    }

    public String buildCreateClientPayload(CreateClientRequest request, String correlationId) {
        Context context = new Context();

        context.setVariable("request", request);
        context.setVariable("sessionContextStr", ""); 
        context.setVariable("userInfo", "officer=\"WX_ADMIN\"");
        context.setVariable("correlationId", correlationId);

        log.info("Building SOAP payload for Client Registration. CorrelationID: {}", correlationId);

        return xmlTemplateEngine.process("create_client_v4", context);
    }

    public String buildCreateClientAddressPayload(CreateClientRequest.AddressInfo addressInfo, String reason, String clientId, String correlationId) {
        Context context = new Context();
        context.setVariable("addressInfo", addressInfo);
        context.setVariable("reason", reason);
        context.setVariable("clientId", clientId);
        context.setVariable("sessionContextStr", "");
        context.setVariable("userInfo", "officer=\"WX_ADMIN\"");
        context.setVariable("correlationId", correlationId);

        return xmlTemplateEngine.process("create_client_address_v2", context);
    }

    public String buildCreateContractPayload(CreateClientRequest clientRequest, Long clientId, String correlationId) {
        Context context = new Context();
        context.setVariable("sessionContextStr", "");
        context.setVariable("userInfo", "officer=\"WX_ADMIN\"");
        context.setVariable("correlationId", correlationId);
        
        context.setVariable("clientSearchMethod", "CLIENT_ID");
        context.setVariable("clientIdentifier", clientId);
        context.setVariable("reason", "create Contract");
        
        context.setVariable("branch", clientRequest.getClientInfo().getBranchCode());
        context.setVariable("institutionCode", clientRequest.getClientInfo().getInstitutionCode());
        context.setVariable("productCode", "LIAB_TRAINING01");
        context.setVariable("contractName", "Liability Contract");
        context.setVariable("cbsNumber", "21324556600");

        log.info("Building SOAP payload for Contract Creation. CorrelationID: {}", correlationId);

        return xmlTemplateEngine.process("create_contract_v4", context);
    }

    public String buildCreateIssuingContractWithLiabilityPayload(com.payment.service.dto.request.CreateIssuingContractWithLiabilityRequest request, String correlationId) {
        Context context = new Context();
        context.setVariable("sessionContextStr", "");
        context.setVariable("userInfo", "officer=\"WX_ADMIN\"");
        context.setVariable("correlationId", correlationId);

        context.setVariable("liabContractSearchMethod", "CONTRACT_NUMBER");
        context.setVariable("clientSearchMethod", "CLIENT_ID");
        context.setVariable("request", request);

        log.info("Building SOAP payload for CreateIssuingContractWithLiability. CorrelationID: {}", correlationId);

        return xmlTemplateEngine.process("create_issuing_contract_with_liability_v2", context);
    }
}
