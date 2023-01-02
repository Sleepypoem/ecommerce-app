package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutDto implements IDto {
    @Override
    public Long getId() {
        return null;
    }
}
