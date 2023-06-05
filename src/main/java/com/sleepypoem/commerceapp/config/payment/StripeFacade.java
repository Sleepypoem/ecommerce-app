package com.sleepypoem.commerceapp.config.payment;

import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;

import java.util.List;

public interface StripeFacade {

    String createCustomer(String email) throws Exception;

    PaymentMethod createPaymentMethod(String cardToken) throws Exception;

    PaymentMethod updatePaymentMethod(String paymentMethodId, String cardToken) throws Exception;

    String attachPaymentMethod(String customerId, String paymentMethodId) throws Exception;

    Customer getCustomer(String customerId) throws Exception;

    PaymentMethod getPaymentMethod(String paymentMethodId) throws Exception;

    List<PaymentMethod> getPaymentMethods(String customerId) throws Exception;

    PaymentIntent getPaymentIntent(String paymentIntentId) throws Exception;

    PaymentIntent createAndConfirmPaymentIntent(String customerId, String paymentMethodId, int amount, String currency) throws Exception;

    PaymentIntent confirmPaymentIntent(String paymentIntentId) throws Exception;

    PaymentIntent cancelPaymentIntent(String paymentIntentId) throws Exception;

    String capturePaymentIntent(String paymentIntentId) throws Exception;

    String refundPaymentIntent(String paymentIntentId) throws Exception;

    String createRefund(String paymentIntentId, String amount) throws Exception;

    String createCharge(String customerId, String paymentMethodId, String amount) throws Exception;

    String captureCharge(String chargeId) throws Exception;

    String refundCharge(String chargeId) throws Exception;

    String createRefundCharge(String chargeId, String amount) throws Exception;

}
