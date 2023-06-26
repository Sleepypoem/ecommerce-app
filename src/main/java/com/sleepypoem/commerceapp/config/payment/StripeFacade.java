package com.sleepypoem.commerceapp.config.payment;

import com.sleepypoem.commerceapp.domain.dto.PaymentIntentDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.exceptions.MyStripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;

public interface StripeFacade {

    String createCustomer(String email) throws MyStripeException;

    PaymentMethodEntity createPaymentMethod(String userId, String cardToken) throws MyStripeException;

    PaymentMethodEntity updatePaymentMethod(PaymentMethodEntity paymentMethodEntity, String cardToken) throws MyStripeException;

    String attachPaymentMethod(String customerId, String paymentMethodId) throws MyStripeException;

    Customer getCustomer(String customerId) throws MyStripeException;

    void deletePaymentMethod(String paymentMethodId) throws MyStripeException;

    PaymentMethod getPaymentMethod(String paymentMethodId) throws MyStripeException;

    PaymentIntent getPaymentIntent(String paymentIntentId) throws MyStripeException;

    PaymentIntentDto createAndConfirmPaymentIntent(String customerId, String paymentMethodId, int amount, String currency) throws MyStripeException;

}
