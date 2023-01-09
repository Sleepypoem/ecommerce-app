package com.sleepypoem.commerceapp.utils;

import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity implements IEntity {
    private Long id;

    private String property1;

    private int property2;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\" : \"" + id + "\"" +
                ", \"property1\" : \"" + property1 + "\"" +
                ", \"property2\" : \"" + property2 + "\"" +
                '}';
    }
}
