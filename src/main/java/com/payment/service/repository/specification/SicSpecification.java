package com.payment.service.repository.specification;

import com.payment.service.dto.request.SicSearchRequest;
import com.payment.service.entity.Sic;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SicSpecification {

    private SicSpecification() {}

    public static Specification<Sic> buildSearch(SicSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Luôn chỉ lấy bản ghi đang Active
            predicates.add(cb.equal(root.get("amndState"), "A"));

            if (request == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            if (StringUtils.hasText(request.getCode())) {
                predicates.add(cb.like(
                        cb.lower(root.get("code")),
                        "%" + request.getCode().trim().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(request.getName())) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + request.getName().trim().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(request.getGroupCode())) {
                predicates.add(cb.equal(
                        cb.lower(root.get("groupCode")),
                        request.getGroupCode().trim().toLowerCase()
                ));
            }

            if (StringUtils.hasText(request.getCustomCode())) {
                predicates.add(cb.equal(
                        cb.lower(root.get("customCode")),
                        request.getCustomCode().trim().toLowerCase()
                ));
            }

            if (StringUtils.hasText(request.getUseInBank())) {
                predicates.add(cb.equal(
                        cb.upper(root.get("useInBank")),
                        request.getUseInBank().trim().toUpperCase()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
