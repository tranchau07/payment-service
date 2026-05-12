package com.payment.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public abstract class BaseCatalogController<R, S extends com.payment.service.service.BaseCatalogService<?, R>> {

    protected abstract S getService();

    @GetMapping("/{code}")
    public R getByCode(@PathVariable("code") String code) {
        return getService().getByCode(code);
    }

    @GetMapping("")
    public List<R> getAll() {
        return getService().getAll();
    }
}
