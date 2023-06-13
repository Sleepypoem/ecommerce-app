package com.sleepypoem.commerceapp.config.payment;

import com.sleepypoem.commerceapp.domain.dto.PaymentIntentDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;

import java.util.List;

public interface StripeFacade {

    String createCustomer(String email) throws StripeException;

    PaymentMethodEntity createPaymentMethod(String userId, String cardToken) throws StripeException;

    PaymentMethodEntity updatePaymentMethod(PaymentMethodEntity paymentMethodEntity, String cardToken) throws StripeException;

    String attachPaymentMethod(String customerId, String paymentMethodId) throws StripeException;

    Customer getCustomer(String customerId) throws StripeException;

    PaymentMethod getPaymentMethod(String paymentMethodId) throws StripeException;

    PaymentIntent getPaymentIntent(String paymentIntentId) throws StripeException;

    PaymentIntentDto createAndConfirmPaymentIntent(String customerId, String paymentMethodId, int amount, String currency) throws StripeException;

}
