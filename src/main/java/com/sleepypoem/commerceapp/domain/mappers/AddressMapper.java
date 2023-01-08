package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper extends SimpleMapper<AddressDto, AddressEntity> {
    @Override
    public AddressDto getDtoInstance() {
        return new AddressDto();
    }

    @Override
    public AddressEntity getEntityInstance() {
        return new AddressEntity();
    }
}
