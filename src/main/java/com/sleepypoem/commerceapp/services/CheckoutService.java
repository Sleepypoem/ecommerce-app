package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.*;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.repositories.CheckoutRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CheckoutService extends AbstractService<CheckoutEntity> {


    @Autowired
    IValidator<CheckoutEntity> validateCheckout;

    @Autowired
    CheckoutRepository dao;

    @Autowired
    CheckoutItemService checkoutItemService;

    @Autowired
    ProductService productService;

    @Override
    protected JpaRepository<CheckoutEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected IValidator<CheckoutEntity> getValidator() {
        return validateCheckout;
    }

    @Override
    public CheckoutEntity create(CheckoutEntity checkout) {
        reserveProducts(checkout);
        checkout.setStatus(CheckoutStatus.PENDING);
        return super.create(checkout);
    }

    private void reserveProducts(CheckoutEntity checkout) {
        for (CheckoutItemEntity item : checkout.getItems()) {
            ProductEntity product = productService.getOneById(item.getProduct().getId());
            log.info(String.valueOf(item.getQuantity()));
            productService.modifyStock(product.getId(), product.getStock() - item.getQuantity());
        }
    }

    private void returnProducts(CheckoutItemEntity item) {
        productService.modifyStock(item.getProduct().getId(), item.getProduct().getStock() + item.getQuantity());
    }

    public List<CheckoutEntity> getByUserId(String userId) {
        return dao.findByUserId(userId);
    }

    public CheckoutEntity addItems(Long id, List<CheckoutItemEntity> checkoutItems) {
        CheckoutEntity checkout = getOneById(id);

        List<CheckoutItemEntity> items = checkout.getItems();
        items.addAll(checkoutItems);
        checkout.setItems(items);
        reserveProducts(checkout);
        log.info(checkout.toString());
        return update(id, checkout);
    }

    public CheckoutEntity removeItem(Long id, Long checkoutItemId) {

        CheckoutEntity checkout = getOneById(id);
        CheckoutItemEntity checkoutItem = checkoutItemService.getOneById(checkoutItemId);

        List<CheckoutItemEntity> items = checkout.getItems();
        items.remove(checkoutItem);
        checkout.setItems(items);
        returnProducts(checkoutItem);
        log.info(checkout.toString());
        CheckoutEntity result = dao.save(checkout);

        if (result.getItems().isEmpty()) {
            getDao().deleteById(id);
            return null;
        }
        return result;
    }

    public CheckoutEntity modifyItemQuantity(Long id, Long itemId, int quantity) {
        CheckoutEntity checkout = getOneById(id);
        CheckoutItemEntity item = checkoutItemService.getOneById(itemId);

        CheckoutItemEntity modifiedItem = checkoutItemService.modifyQuantity(itemId, quantity);
        List<CheckoutItemEntity> items = checkout.getItems();
        items = items.stream().filter(i -> !i.equals(item)).collect(Collectors.toList());
        items.add(modifiedItem);

        return dao.save(checkout);
    }

    public CheckoutEntity addPreferredAddress(Long id, AddressEntity address) {
        CheckoutEntity checkout = getOneById(id);
        checkout.setAddress(address);
        return dao.save(checkout);
    }

    public void setStatusToCompleted(Long id) {
        CheckoutEntity checkout = getOneById(id);
        checkout.setStatus(CheckoutStatus.COMPLETED);
        dao.save(checkout);
    }

    public CheckoutEntity addPreferredPaymentMethod(Long id, PaymentMethodEntity paymentMethod) {
        CheckoutEntity checkout = getOneById(id);
        checkout.setPaymentMethod(paymentMethod);
        return dao.save(checkout);
    }
}
