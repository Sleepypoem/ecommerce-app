package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.PaymentDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper extends SimpleMapper<PaymentDto, PaymentEntity> {
    @Override
    public PaymentDto getDtoInstance() {
        return new PaymentDto();
    }

    @Override
    public PaymentEntity getEntityInstance() {
        return new PaymentEntity();
    }
}
