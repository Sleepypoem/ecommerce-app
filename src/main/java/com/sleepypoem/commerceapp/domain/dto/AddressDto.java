package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class AddressDto extends AbstractDto<Long> {

    private String userId;

    private String country;

    private String state;

    private String zipCode;

    private String firstLine;

    private String secondLine;

    @Override
    public String toString() {
        return "AddressDto{" +
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
