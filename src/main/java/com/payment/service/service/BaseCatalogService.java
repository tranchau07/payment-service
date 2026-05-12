package com.payment.service.service;

import com.payment.service.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseCatalogService<E, R> {

    protected abstract BaseRepository<E, Long> getRepository();
    protected abstract R toResponse(E entity);

    public R getByCode(String code) {
        log.info("Getting {} by code: {}", getEntityName(), code);
        return getRepository().findByCode(code)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException(getEntityName() + " not found with code: " + code));
    }

    public List<R> getAll() {
        log.info("Getting all {}", getEntityName());
        return getRepository().findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    protected abstract String getEntityName();
}
