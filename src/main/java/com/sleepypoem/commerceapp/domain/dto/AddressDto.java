package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressDto implements IDto {
    private Long id;

    private String userId;

    private String country;

    private String state;

    private String zipCode;

    private String firstLine;

    private String secondLine;
}
