package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Table(name = "products")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends AbstractEntity<Long> {

    @Column
    @NotNull
    private String name;

    @Column
    @Min(0)
    private int stock;

    @Column
    private String description;

    @Column
    @Min(0)
    private Double price;

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
