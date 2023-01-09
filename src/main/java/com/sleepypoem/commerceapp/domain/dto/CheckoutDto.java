package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CheckoutDto implements IDto {
    private Long id;
    private String userId;
    private List<ProductEntity> products;

    @Override
    public Long getId() {
        return id;
    }

}
