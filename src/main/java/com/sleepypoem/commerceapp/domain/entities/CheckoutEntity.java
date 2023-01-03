package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class CheckoutEntity implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String user_id;

    private List<ProductEntity> products;

    @Override
    public Long getId(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckoutEntity that = (CheckoutEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
