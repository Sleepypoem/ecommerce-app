package com.sleepypoem.commerceapp.domain.dto;


import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto implements IDto {
    private long id;
    private String name;
    private int stock;
    private String description;
    private double price;


    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getId() {
        return id;
    }
}
