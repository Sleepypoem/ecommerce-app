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
