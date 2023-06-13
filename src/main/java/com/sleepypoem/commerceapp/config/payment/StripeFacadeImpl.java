package com.sleepypoem.commerceapp.config.payment;

import com.sleepypoem.commerceapp.domain.dto.PaymentIntentDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
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

    private static final String CUSTOMER = "customer";

    private static final String TOKEN = "token";

    public StripeFacadeImpl(StripePropertyLoader stripePropertyLoader) {
        this.stripePropertyLoader = stripePropertyLoader;
    }

    @Override
    public String createCustomer(String id) throws StripeException {
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
    public PaymentMethodEntity createPaymentMethod(String userId, String cardToken) throws StripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        PaymentMethodEntity entity = new PaymentMethodEntity();

        PaymentMethodCreateParams params =
                PaymentMethodCreateParams.builder()
                        .setType(PaymentMethodCreateParams.Type.CARD)
                        .setCard(PaymentMethodCreateParams.CardDetails.builder().putExtraParam(TOKEN, cardToken).build())
                        .build();
        PaymentMethod paymentMethod = PaymentMethod.create(params);
        String customerId = createCustomer(userId);
        attachPaymentMethod(customerId, paymentMethod.getId());

        entity.setUserId(userId);
        entity.setStripeUserId(customerId);
        entity.setPaymentId(paymentMethod.getId());
        entity.setPaymentType(paymentMethod.getType());
        entity.setBrand(paymentMethod.getCard().getBrand());
        entity.setLast4(paymentMethod.getCard().getLast4());
        entity.setExpMonth(String.valueOf(paymentMethod.getCard().getExpMonth()));
        entity.setExpYear(String.valueOf(paymentMethod.getCard().getExpYear()));
        return entity;
    }

    @Override
    public PaymentMethodEntity updatePaymentMethod(PaymentMethodEntity paymentMethodEntity, String cardToken) throws StripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();

        PaymentMethod paymentMethod = getPaymentMethod(paymentMethodEntity.getPaymentId());
        PaymentMethod updatedPaymentMethod = paymentMethod.update(
                PaymentMethodUpdateParams.builder()
                        .setCard(PaymentMethodUpdateParams.Card.builder().putExtraParam(TOKEN, cardToken).build())
                        .build()
        );
        paymentMethodEntity.setBrand(updatedPaymentMethod.getCard().getBrand());
        paymentMethodEntity.setLast4(updatedPaymentMethod.getCard().getLast4());
        paymentMethodEntity.setExpMonth(String.valueOf(updatedPaymentMethod.getCard().getExpMonth()));
        paymentMethodEntity.setExpYear(String.valueOf(updatedPaymentMethod.getCard().getExpYear()));
        return paymentMethodEntity;
    }

    @Override
    public String attachPaymentMethod(String customerId, String paymentMethodId) throws StripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        PaymentMethod paymentMethod = getPaymentMethod(paymentMethodId);
        Map<String, Object> params = new HashMap<>();
        params.put(CUSTOMER, customerId);

        PaymentMethod updatedPaymentMethod = paymentMethod.attach(params);

        return updatedPaymentMethod.getId();
    }

    @Override
    public Customer getCustomer(String customerId) throws StripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        return Customer.retrieve(customerId);
    }

    @Override
    public PaymentMethod getPaymentMethod(String paymentMethodId) throws StripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        return PaymentMethod.retrieve(paymentMethodId);
    }

    @Override
    public PaymentIntent getPaymentIntent(String paymentIntentId) throws StripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        return PaymentIntent.retrieve(paymentIntentId);
    }

    @Override
    public PaymentIntentDto createAndConfirmPaymentIntent(String customerId, String paymentMethodId, int amount, String currency) throws StripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        PaymentIntent paymentIntent = PaymentIntent.create(
                Map.of(
                        "amount", amount,
                        "currency", currency,
                        CUSTOMER, customerId,
                        "payment_method", paymentMethodId,
                        "off_session", true,
                        "confirm", true
                )
        );
        return new PaymentIntentDto(paymentIntent.getStatus(), paymentIntent.getId(), paymentIntent.getAmount());
    }
}
