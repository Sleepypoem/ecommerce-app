package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.PaymentMethodDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper extends SimpleMapper<PaymentMethodDto, PaymentMethodEntity> {
    @Override
    public PaymentMethodDto getDtoInstance() {
        return new PaymentMethodDto();
    }

    @Override
    public PaymentMethodEntity getEntityInstance() {
        return new PaymentMethodEntity();
    }
}
