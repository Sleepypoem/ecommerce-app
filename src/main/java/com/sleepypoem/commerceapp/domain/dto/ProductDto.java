package com.sleepypoem.commerceapp.domain.dto;


import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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
}
