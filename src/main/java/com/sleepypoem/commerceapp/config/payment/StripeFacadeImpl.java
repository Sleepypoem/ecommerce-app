package com.sleepypoem.commerceapp.config.payment;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PaymentMethodUpdateParams;
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
    public String createCustomer(String id) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        log.info("Creating customer: email={}", id);
        Map<String, Object> params = new HashMap<>();
        params.put("name", id);
        params.put("description", "Customer for " + id);
        Customer customer;
        customer = Customer.create(params);

        return customer.getId();
    }

    @Override
    public PaymentMethod createPaymentMethod(String cardToken) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();

        PaymentMethodCreateParams params =
                PaymentMethodCreateParams.builder()
                        .setType(PaymentMethodCreateParams.Type.CARD)
                        .setCard(PaymentMethodCreateParams.CardDetails.builder().putExtraParam("token", cardToken).build())
                        .build();

        return PaymentMethod.create(params);
    }

    @Override
    public PaymentMethod updatePaymentMethod(String paymentMethodId, String cardToken) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();

        PaymentMethod paymentMethod = getPaymentMethod(paymentMethodId);
        return paymentMethod.update(
                PaymentMethodUpdateParams.builder()
                        .setCard(PaymentMethodUpdateParams.Card.builder().putExtraParam("token", cardToken).build())
                        .build()
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
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        return PaymentIntent.retrieve(paymentIntentId);
    }

    @Override
    public PaymentIntent createAndConfirmPaymentIntent(String customerId, String paymentMethodId, int amount) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        return PaymentIntent.create(
                Map.of(
                        "amount", amount,
                        "currency", "usd",
                        "customer", customerId,
                        "payment_method", paymentMethodId,
                        "off_session", true,
                        "confirm", true
                )
        );
    }

    @Override
    public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        PaymentIntent paymentIntent = getPaymentIntent(paymentIntentId);
        return paymentIntent.confirm();
    }

    @Override
    public PaymentIntent cancelPaymentIntent(String paymentIntentId) throws Exception {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        PaymentIntent paymentIntent = getPaymentIntent(paymentIntentId);
        return paymentIntent.cancel();
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
