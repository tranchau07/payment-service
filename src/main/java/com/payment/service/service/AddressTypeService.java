package com.payment.service.service;

import com.payment.service.dto.response.AddressTypeResponse;
import com.payment.service.entity.AddressType;
import com.payment.service.mapper.AddressTypeMapper;
import com.payment.service.repository.AddressTypeRepository;
import com.payment.service.repository.BaseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class AddressTypeService extends BaseCatalogService<AddressType, AddressTypeResponse> {
    AddressTypeRepository addressTypeRepository;
    AddressTypeMapper addressTypeMapper;

    @Override
    protected BaseRepository<AddressType, Long> getRepository() {
        return addressTypeRepository;
    }

    @Override
    protected AddressTypeResponse toResponse(AddressType entity) {
        return addressTypeMapper.toAddressTypeResponse(entity);
    }

    @Override
    protected String getEntityName() {
        return "AddressType";
    }

    // Keep original method names if needed for compatibility, or just use inherited ones
    public AddressTypeResponse getAddressTypeByCode(String code) {
        return getByCode(code);
    }

    public List<AddressTypeResponse> getAllSa() {
        return getAll();
    }
}
