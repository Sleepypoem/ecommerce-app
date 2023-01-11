package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.ReceiptDto;
import com.sleepypoem.commerceapp.domain.entities.ReceiptEntity;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMapper extends SimpleMapper<ReceiptDto, ReceiptEntity> {
    @Override
    public ReceiptDto getDtoInstance() {
        return new ReceiptDto();
    }

    @Override
    public ReceiptEntity getEntityInstance() {
        return new ReceiptEntity();
    }
}
