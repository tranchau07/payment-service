package com.payment.service.util;

import com.payment.service.dto.response.CreateClientResponse;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

public class XmlParserUtil {

    public static CreateClientResponse parseCreateClientResponse(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));

            XPath xPath = XPathFactory.newInstance().newXPath();

            String newClient = xPath.evaluate("//NewClient", doc);
            String appNumber = xPath.evaluate("//ApplicationNumber", doc);
            String retCode = xPath.evaluate("//RetCode", doc);
            String retMsg = xPath.evaluate("//RetMsg", doc);
            String debugInfo = xPath.evaluate("//DebugInfo", doc);
            String resultInfo = xPath.evaluate("//ResultInfo", doc);

            return CreateClientResponse.builder()
                    .newClientId(newClient.isEmpty() ? null : Long.parseLong(newClient))
                    .applicationNumber(appNumber)
                    .retCode(retCode.isEmpty() ? null : Long.parseLong(retCode))
                    .retMsg(retMsg)
                    .debugInfo(debugInfo)
                    .resultInfo(resultInfo)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML response", e);
        }
    }
}