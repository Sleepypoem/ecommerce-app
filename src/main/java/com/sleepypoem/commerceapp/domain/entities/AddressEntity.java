package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class AddressEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    private String country;

    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "first_ine")
    private String firstLine;

    @Column(name = "second_line")
    private String secondLine;

    @Override
    public Long getId() {
        return id;
    }
}
