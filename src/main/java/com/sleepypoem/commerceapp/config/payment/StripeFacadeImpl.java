package com.sleepypoem.commerceapp.config.payment;

import com.sleepypoem.commerceapp.domain.dto.PaymentIntentDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.exceptions.MyStripeException;
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
    public String createCustomer(String id) throws MyStripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        log.info("Creating customer: email={}", id);
        Map<String, Object> params = new HashMap<>();
        params.put("name", id);
        params.put("description", "Customer for " + id);
        Customer customer;
        try {
            customer = Customer.create(params);
        } catch (StripeException e) {
            handleStripeException(e);
            return null;
        }

        return customer.getId();
    }

    @Override
    public PaymentMethodEntity createPaymentMethod(String userId, String cardToken) throws MyStripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        PaymentMethodEntity entity = new PaymentMethodEntity();

        PaymentMethodCreateParams params =
                PaymentMethodCreateParams.builder()
                        .setType(PaymentMethodCreateParams.Type.CARD)
                        .setCard(PaymentMethodCreateParams.CardDetails.builder().putExtraParam(TOKEN, cardToken).build())
                        .build();
        PaymentMethod paymentMethod = null;
        String customerId = null;
        try {
            paymentMethod = PaymentMethod.create(params);
            customerId = createCustomer(userId);
            attachPaymentMethod(customerId, paymentMethod.getId());
        } catch (StripeException e) {
            handleStripeException(e);
        }

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
    public PaymentMethodEntity updatePaymentMethod(PaymentMethodEntity paymentMethodEntity, String cardToken) throws MyStripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();

        PaymentMethod updatedPaymentMethod = null;
        try {
            PaymentMethod paymentMethod = getPaymentMethod(paymentMethodEntity.getPaymentId());
            updatedPaymentMethod = paymentMethod.update(
                    PaymentMethodUpdateParams.builder()
                            .setCard(PaymentMethodUpdateParams.Card.builder().putExtraParam(TOKEN, cardToken).build())
                            .build()
            );
        } catch (StripeException e) {
            handleStripeException(e);
        }
        paymentMethodEntity.setBrand(updatedPaymentMethod.getCard().getBrand());
        paymentMethodEntity.setLast4(updatedPaymentMethod.getCard().getLast4());
        paymentMethodEntity.setExpMonth(String.valueOf(updatedPaymentMethod.getCard().getExpMonth()));
        paymentMethodEntity.setExpYear(String.valueOf(updatedPaymentMethod.getCard().getExpYear()));
        return paymentMethodEntity;
    }

    @Override
    public String attachPaymentMethod(String customerId, String paymentMethodId) throws MyStripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        Map<String, Object> params = new HashMap<>();
        params.put(CUSTOMER, customerId);

        PaymentMethod updatedPaymentMethod = null;
        try {
            PaymentMethod paymentMethod = getPaymentMethod(paymentMethodId);
            updatedPaymentMethod = paymentMethod.attach(params);
        } catch (StripeException e) {
            handleStripeException(e);
        }

        return updatedPaymentMethod.getId();
    }

    @Override
    public Customer getCustomer(String customerId) throws MyStripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        Customer customer = null;
        try {
            customer = Customer.retrieve(customerId);
        } catch (StripeException e) {
            handleStripeException(e);
        }

        return customer;
    }

    @Override
    public void deletePaymentMethod(String paymentMethodId) throws MyStripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        PaymentMethod paymentMethod = null;
        try {
            paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            paymentMethod.detach();
        } catch (StripeException e) {
            handleStripeException(e);
        }
    }

    @Override
    public PaymentMethod getPaymentMethod(String paymentMethodId) throws MyStripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        try {
            return PaymentMethod.retrieve(paymentMethodId);
        } catch (StripeException e) {
            handleStripeException(e);
            return null;
        }
    }

    @Override
    public PaymentIntent getPaymentIntent(String paymentIntentId) throws MyStripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        try {
            return PaymentIntent.retrieve(paymentIntentId);
        } catch (StripeException e) {
            handleStripeException(e);
            return null;
        }
    }

    @Override
    public PaymentIntentDto createAndConfirmPaymentIntent(String customerId, String paymentMethodId, int amount, String currency) throws MyStripeException {
        Stripe.apiKey = stripePropertyLoader.getSecretKey();
        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.create(
                    Map.of(
                            "amount", amount,
                            "currency", currency,
                            CUSTOMER, customerId,
                            "payment_method", paymentMethodId,
                            "off_session", true,
                            "confirm", true
                    )
            );
        } catch (StripeException e) {
            handleStripeException(e);
            return null;
        }
        log.info("Amount: {}", paymentIntent.getAmount());
        return new PaymentIntentDto(paymentIntent.getStatus(), paymentIntent.getId(), paymentIntent.getAmount());
    }

    private void handleStripeException(StripeException e) throws MyStripeException {
        throw new MyStripeException(e.getMessage());
    }
}
