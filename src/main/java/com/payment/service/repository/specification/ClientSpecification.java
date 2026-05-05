package com.payment.service.repository.specification;

import com.payment.service.dto.request.ClientSearchRequest;
import com.payment.service.entity.Client;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecification {

    private ClientSpecification() {}

    public static Specification<Client> buildSearch(ClientSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Luôn chỉ lấy các bản ghi đang Active
            predicates.add(cb.equal(root.get("amndState"), "A"));

            if (request == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            if (StringUtils.hasText(request.getBranchCode())) {
                predicates.add(cb.equal(root.get("branchCode"), request.getBranchCode()));
            }

            if (StringUtils.hasText(request.getShortName())) {
                predicates.add(cb.like(
                        cb.lower(root.get("shortName")),
                        "%" + request.getShortName().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(request.getClientNumber())) {
                predicates.add(cb.like(root.get("clientNumber"), "%" + request.getClientNumber() + "%"));
            }

            if (StringUtils.hasText(request.getItn())) {
                predicates.add(cb.like(root.get("itn"), "%" + request.getItn() + "%"));
            }

            if (StringUtils.hasText(request.getPhoneNumber())) {
                String phonePattern = "%" + request.getPhoneNumber() + "%";
                predicates.add(cb.or(
                        cb.like(root.get("phoneMobile"), phonePattern),
                        cb.like(root.get("phoneHome"), phonePattern),
                        cb.like(root.get("phone"), phonePattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}