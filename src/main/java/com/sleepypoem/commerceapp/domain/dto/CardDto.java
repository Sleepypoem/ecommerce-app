package com.sleepypoem.commerceapp.domain.dto;

import lombok.Data;

@Data
public class CardDto {

    private String userId;

    private String cardToken;

    @Override
    public String toString() {
        return "CreateCardDto{" +
                "userId='" + userId + '\'' +
                ", cardToken='" + cardToken + '\'' +
                '}';
    }
}
