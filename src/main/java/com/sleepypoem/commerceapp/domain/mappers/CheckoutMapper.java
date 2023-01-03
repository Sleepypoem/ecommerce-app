package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CheckoutMapper extends SimpleMapper<CheckoutDto, CheckoutEntity> {

    @Override
    public CheckoutDto getDtoInstance() {
        return new CheckoutDto();
    }

    @Override
    public CheckoutEntity getEntityInstance() {
        return new CheckoutEntity();
    }
}
