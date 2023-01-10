package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.CheckoutItemDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import org.springframework.stereotype.Component;

@Component
public class CheckoutItemMapper extends SimpleMapper<CheckoutItemDto, CheckoutItemEntity> {
    @Override
    public CheckoutItemDto getDtoInstance() {
        return new CheckoutItemDto();
    }

    @Override
    public CheckoutItemEntity getEntityInstance() {
        return new CheckoutItemEntity();
    }
}
