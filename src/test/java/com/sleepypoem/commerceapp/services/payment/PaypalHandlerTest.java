package com.sleepypoem.commerceapp.services.payment;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaypalHandlerTest {

    private PaypalHandler paypalHandler;

    @BeforeEach
    void init() {

        AddressEntity addressDto = AddressEntity
                .builder()
                .id(1L)
                .country("US")
                .state("Arkansas")
                .zipCode("45789")
                .firstLine("TestAddress")
                .userId("TestUser")
                .build();

        PaymentMethodEntity paymentMethodDto = PaymentMethodEntity
                .builder()
                .paymentId("TestPaymentId")
                .id(1L)
                .userId("TestUser")
                .paymentType("PAYPAL")
                .build();

        ProductEntity product = ProductEntity
                .builder()
                .id(1L)
                .name("TestProduct")
                .description("TestDescription")
                .price(22.50)
                .stock(23)
                .build();

        List<CheckoutItemEntity> items = new ArrayList<>();
        items.add(CheckoutItemEntity.builder().id(1L).product(product).build());

        CheckoutDto checkout = CheckoutDto
                .builder()
                .id(1L)
                .userId("testUser")
                .address(addressDto)
                .items(items)
                .paymentMethod(paymentMethodDto)
                .build();

        UserDto user = UserDto.builder()
                .id("testUser")
                .firstName("Juan")
                .lastName("Perez")
                .email("JuanPe@gmail.com")
                .username("JuanPe")
                .build();
        PaymentRequestDto paymentRequest = new PaymentRequestDto(checkout, user);
        paypalHandler = new PaypalHandler();

        paypalHandler.setPaymentMethod(paymentRequest);
    }

    @Test
    void testCanHandleWhenPaymentTypePaypal() {

        boolean actual = paypalHandler.canHandle();
        assertTrue(actual);
    }

    @Test
    void testHandlePaymentRequest() {
        PaymentDto actual = paypalHandler.handle();
        assertInstanceOf(PaymentDto.class, actual);
    }
}
