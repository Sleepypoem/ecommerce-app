package com.sleepypoem.commerceapp.config.payment;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StripeFacadeImpl implements StripeFacade {

    private final StripePropertyLoader stripePropertyLoader;

    public StripeFacadeImpl(StripePropertyLoader stripePropertyLoader) {
        this.stripePropertyLoader = stripePropertyLoader;
    }

    @Override
    public String createCustomer(String email) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        log.info("Creating customer: email={}", email);
        Customer customer = Customer.create(
                CustomerCreateParams.builder()
                        .setEmail(email)
                        .build()
        );
        return customer.getId();
    }

    @Override
    public PaymentMethod createPaymentMethod(String cardToken) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();

        return PaymentMethod.create(
                Map.of("type", "card", "card", cardToken)
        );
    }

    @Override
    public String attachPaymentMethod(String customerId, String paymentMethodId) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        PaymentMethod paymentMethod = getPaymentMethod(paymentMethodId);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);

        PaymentMethod updatedPaymentMethod =
                paymentMethod.attach(params);

        return updatedPaymentMethod.getId();
    }

    @Override
    public Customer getCustomer(String customerId) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        return Customer.retrieve(customerId);
    }

    @Override
    public PaymentMethod getPaymentMethod(String paymentMethodId) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        return PaymentMethod.retrieve(paymentMethodId);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods(String customerId) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        return PaymentMethod.list(
                Map.of("customer", customerId, "type", "card")
        ).getData();
    }

    @Override
    public PaymentIntent getPaymentIntent(String paymentIntentId) throws Exception {
        return null;
    }

    @Override
    public String createPaymentIntent(String customerId, String paymentMethodId, String amount) throws Exception {
        return null;
    }

    @Override
    public String capturePaymentIntent(String paymentIntentId) throws Exception {
        return null;
    }

    @Override
    public String refundPaymentIntent(String paymentIntentId) throws Exception {
        return null;
    }

    @Override
    public String createRefund(String paymentIntentId, String amount) throws Exception {
        return null;
    }

    @Override
    public String createCharge(String customerId, String paymentMethodId, String amount) throws Exception {
        return null;
    }

    @Override
    public String captureCharge(String chargeId) throws Exception {
        return null;
    }

    @Override
    public String refundCharge(String chargeId) throws Exception {
        return null;
    }

    @Override
    public String createRefundCharge(String chargeId, String amount) throws Exception {
        return null;
    }
}
