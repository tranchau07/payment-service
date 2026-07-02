package com.payment.service.repository.specification;

import com.payment.service.dto.request.MerchantSearchRequest;
import com.payment.service.entity.Client;
import com.payment.service.entity.AcntContract;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MerchantSpecification {

    private MerchantSpecification() {}

    public static Specification<Client> buildSearch(MerchantSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always only get active records
            predicates.add(cb.equal(root.get("amndState"), "A"));

            // Default base condition for Merchants:
            // CCAT = 'C' (Commercial) OR PCAT = 'M' (Acquiring)
            predicates.add(cb.and(
                    cb.equal(root.get("clientCategory"), "C"),
                    cb.equal(root.get("productCategory"), "M")
            ));

            if (request == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            if (StringUtils.hasText(request.getBranchCode())) {
                predicates.add(cb.equal(root.get("branchCode"), request.getBranchCode()));
            }

            if (StringUtils.hasText(request.getShortName())) {
                String namePattern = "%" + request.getShortName().trim().toLowerCase().replaceAll("\\s+", "%") + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("shortName")), namePattern),
                        cb.like(cb.lower(root.get("firstName")), namePattern),
                        cb.like(cb.lower(root.get("lastName")), namePattern),
                        cb.like(cb.lower(root.get("companyName")), namePattern)
                ));
            }

            if (StringUtils.hasText(request.getClientNumber())) {
                predicates.add(cb.like(
                        cb.lower(root.get("clientNumber")),
                        "%" + request.getClientNumber().trim().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(request.getItn())) {
                predicates.add(cb.like(
                        cb.lower(root.get("itn")),
                        "%" + request.getItn().trim().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(request.getPhoneNumber())) {
                String phonePattern = getCleanPhonePattern(request.getPhoneNumber());
                if (StringUtils.hasText(phonePattern)) {
                    predicates.add(cb.or(
                            cb.like(root.get("phoneMobile"), phonePattern),
                            cb.like(root.get("phoneHome"), phonePattern),
                            cb.like(root.get("phone"), phonePattern)
                    ));
                }
            }

            // Filter by PCAT on Client table
            if (StringUtils.hasText(request.getPcat())) {
                predicates.add(cb.equal(root.get("productCategory"), request.getPcat()));
            }

            // Filter by CCAT on Client table
            if (StringUtils.hasText(request.getCcat())) {
                predicates.add(cb.equal(root.get("clientCategory"), request.getCcat()));
            }

            // Filter by CON_CAT on AcntContract (using subquery)
            if (StringUtils.hasText(request.getConCat())) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<AcntContract> contractRoot = subquery.from(AcntContract.class);
                subquery.select(contractRoot.get("clientId"));
                List<Predicate> subPredicates = new ArrayList<>();
                subPredicates.add(cb.equal(contractRoot.get("conCat"), request.getConCat()));
                subPredicates.add(cb.equal(contractRoot.get("clientId"), root.get("id")));
                subPredicates.add(cb.equal(contractRoot.get("amndState"), "A"));
                subquery.where(subPredicates.toArray(new Predicate[0]));
                predicates.add(cb.exists(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String getCleanPhonePattern(String input) {
        if (!StringUtils.hasText(input)) {
            return "";
        }
        String digits = input.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            return "";
        }
        if (digits.startsWith("84") && digits.length() > 2) {
            digits = digits.substring(2);
        } else if (digits.startsWith("0") && digits.length() > 1) {
            digits = digits.substring(1);
        }
        return "%" + digits + "%";
    }
}
