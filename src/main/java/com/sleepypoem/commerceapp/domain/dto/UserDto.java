package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserDto {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    private List<AddressEntity> addresses = new ArrayList<>();
    private List<CheckoutEntity> checkouts = new ArrayList<>();
    private List<PaymentMethodEntity> paymentMethods = new ArrayList<>();
}
