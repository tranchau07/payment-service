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
        context.setVariable("sessionContextStr", "?");
        context.setVariable("userInfo", "officer=\"WX_ADMIN\"");
        context.setVariable("correlationId", "?");

        log.info("REQUEST = {}", request);
        log.info("CLIENT INFO = {}", request.getClientInfo());

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
}
