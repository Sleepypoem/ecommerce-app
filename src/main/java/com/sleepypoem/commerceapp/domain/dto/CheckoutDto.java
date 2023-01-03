package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CheckoutDto implements IDto {
    @Override
    public Long getId() {
        return null;
    }
}
