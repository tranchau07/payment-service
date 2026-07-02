package com.payment.service.service;

import com.payment.service.dto.response.ProductTreeNodeDto;
import com.payment.service.entity.ApplProduct;
import com.payment.service.repository.AcntContractRepository;
import com.payment.service.repository.ApplProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductTreeService {

    ApplProductRepository applProductRepository;
    AcntContractRepository acntContractRepository;
    JdbcTemplate jdbcTemplate;

    public List<ProductTreeNodeDto> getProductTree() {
        log.info("Fetching all active products to construct hierarchy tree.");
        
        // Fetch all active products
        List<ApplProduct> activeProducts = applProductRepository.findAllByAmndState("A");

        // Fetch real-time active contracts counts grouped by product internal code
        log.info("Querying active contracts count from ACNT_CONTRACT table.");
        List<Object[]> contractCounts = acntContractRepository.countContractsGroupedByProduct("A");
        Map<String, Long> productContractCounts = new HashMap<>();
        for (Object[] row : contractCounts) {
            String productCode = (String) row[0];
            Long count = (Long) row[1];
            if (productCode != null) {
                productContractCounts.put(productCode, count);
            }
        }

        // Load lookups for descriptions
        log.info("Loading catalog lookups for product configuration parameters.");
        Map<Long, String> contrTypeMap = new HashMap<>();
        try {
            List<Map<String, Object>> contrTypes = jdbcTemplate.queryForList(
                "SELECT ID, CODE, NAME FROM INT.CONTR_TYPE WHERE AMND_STATE = 'A'"
            );
            for (Map<String, Object> r : contrTypes) {
                Long id = ((Number) r.get("ID")).longValue();
                String code = (String) r.get("CODE");
                String name = (String) r.get("NAME");
                String desc = (code != null && !code.trim().isEmpty() ? code + " - " : "") + name;
                contrTypeMap.put(id, desc);
            }
        } catch (Exception e) {
            log.error("Failed to load CONTR_TYPE lookup map", e);
        }

        Map<Long, String> contrSubtypeMap = new HashMap<>();
        try {
            List<Map<String, Object>> contrSubtypes = jdbcTemplate.queryForList(
                "SELECT ID, CODE, NAME FROM INT.CONTR_SUBTYPE WHERE AMND_STATE = 'A'"
            );
            for (Map<String, Object> r : contrSubtypes) {
                Long id = ((Number) r.get("ID")).longValue();
                String code = (String) r.get("CODE");
                String name = (String) r.get("NAME");
                String desc = (code != null && !code.trim().isEmpty() ? code + " - " : "") + name;
                contrSubtypeMap.put(id, desc);
            }
        } catch (Exception e) {
            log.error("Failed to load CONTR_SUBTYPE lookup map", e);
        }

        Map<Long, String> accSchemeMap = new HashMap<>();
        try {
            List<Map<String, Object>> accSchemes = jdbcTemplate.queryForList(
                "SELECT ID, CODE, SCHEME_NAME FROM INT.ACC_SCHEME WHERE AMND_STATE = 'A'"
            );
            for (Map<String, Object> r : accSchemes) {
                Long id = ((Number) r.get("ID")).longValue();
                String code = (String) r.get("CODE");
                String name = (String) r.get("SCHEME_NAME");
                String desc = (code != null && !code.trim().isEmpty() ? code + " - " : "") + name;
                accSchemeMap.put(id, desc);
            }
        } catch (Exception e) {
            log.error("Failed to load ACC_SCHEME lookup map", e);
        }

        Map<Long, String> servPackMap = new HashMap<>();
        try {
            List<Map<String, Object>> servPacks = jdbcTemplate.queryForList(
                "SELECT ID, CODE, NAME FROM INT.SERV_PACK WHERE AMND_STATE = 'A'"
            );
            for (Map<String, Object> r : servPacks) {
                Long id = ((Number) r.get("ID")).longValue();
                String code = (String) r.get("CODE");
                String name = (String) r.get("NAME");
                String desc = (code != null && !code.trim().isEmpty() ? code + " - " : "") + name;
                servPackMap.put(id, desc);
            }
        } catch (Exception e) {
            log.error("Failed to load SERV_PACK lookup map", e);
        }
        
        // Map to hold ProductTreeNodeDto mapped by internalCode
        Map<String, ProductTreeNodeDto> nodeMap = new HashMap<>();
        List<ProductTreeNodeDto> roots = new ArrayList<>();

        // First pass: create DTOs and populate map
        for (ApplProduct product : activeProducts) {
            Long count = productContractCounts.getOrDefault(product.getInternalCode(), 0L);
            ProductTreeNodeDto nodeDto = ProductTreeNodeDto.builder()
                    .id(product.getId())
                    .code(product.getCode())
                    .name(product.getName())
                    .internalCode(product.getInternalCode())
                    .parentCode(product.getParentCode())
                    .pcat(product.getPcat())
                    .conCat(product.getConCat())
                    .contrType(product.getContrType())
                    .contrTypeDesc(product.getContrType() != null ? contrTypeMap.get(product.getContrType()) : null)
                    .contrSubtype(product.getContrSubtype())
                    .contrSubtypeDesc(product.getContrSubtype() != null ? contrSubtypeMap.get(product.getContrSubtype()) : null)
                    .accScheme(product.getAccScheme())
                    .accSchemeDesc(product.getAccScheme() != null ? accSchemeMap.get(product.getAccScheme()) : null)
                    .servicePack(product.getServicePack())
                    .servicePackDesc(product.getServicePack() != null ? servPackMap.get(product.getServicePack()) : null)
                    .maxCreditLimit(product.getMaxCreditLimit())
                    .minCreditLimit(product.getMinCreditLimit())
                    .defaultCreditLimit(product.getDefaultCreditLimit())
                    .ncontracts(count.intValue())
                    .isActive(product.getIsActive())
                    .isReady(product.getIsReady())
                    .children(new ArrayList<>())
                    .build();
            
            nodeMap.put(product.getInternalCode(), nodeDto);
        }

        // Second pass: construct tree structure
        for (ProductTreeNodeDto node : nodeMap.values()) {
            String parentCode = node.getParentCode();
            if (parentCode == null || parentCode.trim().isEmpty()) {
                // It is a root node
                roots.add(node);
            } else {
                ProductTreeNodeDto parentNode = nodeMap.get(parentCode);
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                } else {
                    // Parent exists in database but might be inactive/not loaded
                    log.warn("Product {} refers to parent {} which is not in active products. Adding as root.", 
                            node.getCode(), parentCode);
                    roots.add(node);
                }
            }
        }

        log.info("Successfully constructed product tree with {} root nodes.", roots.size());
        return roots;
    }
}
