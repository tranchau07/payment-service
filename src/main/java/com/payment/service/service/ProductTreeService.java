package com.payment.service.service;

import com.payment.service.dto.response.ProductTreeNodeDto;
import com.payment.service.entity.ApplProduct;
import com.payment.service.repository.AcntContractRepository;
import com.payment.service.repository.ApplProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    public List<ProductTreeNodeDto> getProductTree() {
        log.info("Fetching all active products to construct hierarchy tree.");
        
        // Fetch all active products
        List<ApplProduct> activeProducts = applProductRepository.findAllByIsReadyAndAmndState("Y", "A");

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
                    .contrSubtype(product.getContrSubtype())
                    .accScheme(product.getAccScheme())
                    .servicePack(product.getServicePack())
                    .maxCreditLimit(product.getMaxCreditLimit())
                    .minCreditLimit(product.getMinCreditLimit())
                    .defaultCreditLimit(product.getDefaultCreditLimit())
                    .ncontracts(count.intValue())
                    .isActive(product.getIsActive())
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
