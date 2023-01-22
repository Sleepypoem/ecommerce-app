package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressDto implements IDto {
    private Long id;

    private String userId;

    private String country;

    private String state;

    private String zipCode;

    private String firstLine;

    private String secondLine;

    @Override
    public String toString() {
        return "{" + "\n" +
                "id=" + id + "\n" +
                ", userId='" + userId + '\'' + "\n" +
                ", country='" + country + '\'' + "\n" +
                ", state='" + state + '\'' + "\n" +
                ", zipCode='" + zipCode + '\'' + "\n" +
                ", firstLine='" + firstLine + '\'' + "\n" +
                ", secondLine='" + secondLine + '\'' + "\n" +
                '}' +"\n";
    }
}
