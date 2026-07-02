package com.payment.service.service;

import com.payment.service.dto.response.ProductConfigurationResponse;
import com.payment.service.entity.*;
import com.payment.service.exception.AppException;
import com.payment.service.exception.ErrorCode;
import com.payment.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductConfigurationService {
    private static final String ACTIVE = "A";
    private static final String READY = "Y";

    private final ApplProductRepository productRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final ContractSubtypeRepository contractSubtypeRepository;
    private final AccountSchemeRepository accountSchemeRepository;
    private final ServicePackRepository servicePackRepository;

    public enum BusinessType {
        ISSUING("C"), ACQUIRING("M");
        private final String pcat;
        BusinessType(String pcat) { this.pcat = pcat; }
    }

    @Transactional(readOnly = true)
    public List<ProductConfigurationResponse> getProducts(BusinessType type, boolean includeNotReady) {
        List<ApplProduct> products = includeNotReady
                ? productRepository.findAllByPcatAndAmndStateOrderByNameAsc(type.pcat, ACTIVE)
                : productRepository.findAllByPcatAndAmndStateAndIsReadyOrderByNameAsc(type.pcat, ACTIVE, READY);
        return resolve(products, type);
    }

    @Transactional(readOnly = true)
    public ProductConfigurationResponse getByCode(String code) {
        ApplProduct product = productRepository.findByCode(code)
                .filter(p -> ACTIVE.equals(p.getAmndState()))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND, null,
                        "Khong tim thay product dang hoat dong: " + code, null));
        BusinessType type = fromPcat(product.getPcat());
        return resolve(List.of(product), type).getFirst();
    }

    private List<ProductConfigurationResponse> resolve(List<ApplProduct> products, BusinessType type) {
        Set<Long> contractTypeIds = ids(products, ApplProduct::getContrType);
        Set<Long> subtypeIds = ids(products, ApplProduct::getContrSubtype);
        Set<Long> accountSchemeIds = ids(products, ApplProduct::getAccScheme);
        Set<Long> servicePackIds = ids(products, ApplProduct::getServicePack);

        Map<Long, ContractType> contractTypes = contractTypeIds.isEmpty() ? Map.of() : byId(
                contractTypeRepository.findAllByIdInAndAmndState(contractTypeIds, ACTIVE), ContractType::getId);
        Map<Long, ContractSubtype> subtypes = subtypeIds.isEmpty() ? Map.of() : byId(
                contractSubtypeRepository.findAllByIdInAndAmndState(subtypeIds, ACTIVE), ContractSubtype::getId);
        Map<Long, AccountScheme> accountSchemes = accountSchemeIds.isEmpty() ? Map.of() : byId(
                accountSchemeRepository.findAllByIdInAndAmndState(accountSchemeIds, ACTIVE), AccountScheme::getId);
        Map<Long, ServicePack> servicePacks = servicePackIds.isEmpty() ? Map.of() : byId(
                servicePackRepository.findAllByIdInAndAmndState(servicePackIds, ACTIVE), ServicePack::getId);

        return products.stream().map(product -> {
            List<String> missing = new ArrayList<>();
            ContractType contractType = lookup(product.getContrType(), contractTypes, "CONTR_TYPE", missing);
            ContractSubtype subtype = lookup(product.getContrSubtype(), subtypes, "CONTR_SUBTYPE", missing);
            AccountScheme accountScheme = lookup(product.getAccScheme(), accountSchemes, "ACC_SCHEME", missing);
            ServicePack servicePack = lookup(product.getServicePack(), servicePacks, "SERV_PACK", missing);
            return ProductConfigurationResponse.builder()
                    .id(product.getId()).code(product.getCode()).name(product.getName())
                    .internalCode(product.getInternalCode()).businessType(type.name())
                    .pcat(product.getPcat()).conCat(product.getConCat()).ccat(product.getCcat())
                    .parentCode(product.getParentCode())
                    .rootProduct(product.getParentCode() == null || product.getParentCode().isBlank())
                    .isActive(product.getIsActive()).isReady(product.getIsReady())
                    .contractType(toReference(contractType))
                    .contractSubtype(toReference(subtype))
                    .accountScheme(toReference(accountScheme))
                    .servicePack(toReference(servicePack))
                    .missingReferences(missing)
                    .minCreditLimit(product.getMinCreditLimit())
                    .maxCreditLimit(product.getMaxCreditLimit())
                    .defaultCreditLimit(product.getDefaultCreditLimit())
                    .build();
        }).toList();
    }

    private ProductConfigurationResponse.ConfigurationReference toReference(ContractType value) {
        if (value == null) return null;
        return reference(value.getId(), value.getCode(), value.getName(), "CONTR_TYPE", Map.of(
                "contractCategory", nullable(value.getContractCategory()),
                "channel", nullable(value.getChannel()),
                "serviceCode", nullable(value.getServiceCode()),
                "terminalCategory", nullable(value.getTerminalCategory())));
    }

    private ProductConfigurationResponse.ConfigurationReference toReference(ContractSubtype value) {
        if (value == null) return null;
        return reference(value.getId(), value.getCode(), value.getName(), "CONTR_SUBTYPE", Map.of(
                "contractTypeId", nullable(value.getContractTypeId()),
                "contractCategory", nullable(value.getContractCategory()),
                "terminalCategory", nullable(value.getTerminalCategory()),
                "clientCategory", nullable(value.getClientCategory()),
                "channel", nullable(value.getChannel()),
                "isActive", nullable(value.getIsActive())));
    }

    private ProductConfigurationResponse.ConfigurationReference toReference(AccountScheme value) {
        if (value == null) return null;
        return reference(value.getId(), value.getCode(), value.getName(), "ACC_SCHEME", Map.of(
                "productCategory", nullable(value.getProductCategory()),
                "clientCategory", nullable(value.getClientCategory()),
                "currency", nullable(value.getCurrency()),
                "useForContracts", nullable(value.getUseForContracts()),
                "parentSchemeId", nullable(value.getParentSchemeId()),
                "isReady", nullable(value.getIsReady())));
    }

    private ProductConfigurationResponse.ConfigurationReference toReference(ServicePack value) {
        if (value == null) return null;
        return reference(value.getId(), value.getCode(), value.getName(), "SERV_PACK", Map.of(
                "clientCategory", nullable(value.getClientCategory()),
                "contractCategory", nullable(value.getContractCategory()),
                "contractTypeId", nullable(value.getContractTypeId()),
                "servicePackTypeId", nullable(value.getServicePackTypeId()),
                "basePackId", nullable(value.getBasePackId()),
                "parentPackId", nullable(value.getParentPackId()),
                "useForContracts", nullable(value.getUseForContracts()),
                "isReady", nullable(value.getIsReady())));
    }

    private ProductConfigurationResponse.ConfigurationReference reference(
            Long id, String code, String name, String table, Map<String, Object> attributes) {
        String displayName = code == null || code.isBlank() ? name : code + " - " + name;
        return ProductConfigurationResponse.ConfigurationReference.builder()
                .id(id).code(code).name(name).displayName(displayName).sourceTable(table).attributes(attributes).build();
    }

    private BusinessType fromPcat(String pcat) {
        return Arrays.stream(BusinessType.values()).filter(type -> type.pcat.equalsIgnoreCase(pcat))
                .findFirst().orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST_DATA, null,
                        "Product khong thuoc Issuing (PCAT=C) hoac Acquiring (PCAT=M)", null));
    }

    private static Object nullable(Object value) { return value == null ? "" : value; }

    private static <T> T lookup(Long id, Map<Long, T> values, String name, List<String> missing) {
        if (id == null) return null;
        T value = values.get(id);
        if (value == null) missing.add(name + ":" + id);
        return value;
    }

    private static Set<Long> ids(List<ApplProduct> products, Function<ApplProduct, Long> getter) {
        return products.stream().map(getter).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private static <T> Map<Long, T> byId(List<T> values, Function<T, Long> idGetter) {
        return values.stream().collect(Collectors.toMap(idGetter, Function.identity()));
    }
}
