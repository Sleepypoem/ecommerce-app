package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {

private String id;
private String userName;
private String firstName;
private String lastName;
private String email;
private String phone;
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
private String password;

private List<AddressEntity> addresses = new ArrayList<>();
private List<CheckoutEntity> checkouts= new ArrayList<>();
private List<PaymentMethodEntity> paymentMethods = new ArrayList<>();
}
