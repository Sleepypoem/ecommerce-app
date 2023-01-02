package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
}
