package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity extends AbstractEntity<Long> {

    @Column(name = "user_id")
    @NotNull
    private String userId;

    @NotNull
    private String country;

    @NotNull
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "first_line")
    private String firstLine;

    @Column(name = "second_line")
    private String secondLine;

    @Override
    public String toString() {
        return "AddressEntity{" +
                ", id=" + id +
                "userId='" + userId + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", firstLine='" + firstLine + '\'' +
                ", secondLine='" + secondLine + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
