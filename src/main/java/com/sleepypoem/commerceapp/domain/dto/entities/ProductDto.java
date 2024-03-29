package com.sleepypoem.commerceapp.domain.dto.entities;


import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ProductDto extends AbstractDto<Long> {
    private String name;
    private int stock;
    private String description;
    private Double price;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDto<?> that = (AbstractDto<?>) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
