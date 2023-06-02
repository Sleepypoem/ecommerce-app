package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.annotations.ValidableMethod;
import com.sleepypoem.commerceapp.domain.entities.*;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.repositories.CheckoutRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.abstracts.HaveUser;
import com.sleepypoem.commerceapp.services.validators.impl.ValidateCheckout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Validable(ValidateCheckout.class)
@Slf4j
public class CheckoutService extends AbstractService<CheckoutEntity, Long> implements HaveUser<CheckoutEntity> {

    private final CheckoutRepository dao;

    private final CheckoutItemService checkoutItemService;

    private final ProductService productService;

    public CheckoutService(CheckoutRepository dao, CheckoutItemService checkoutItemService, ProductService productService) {
        this.dao = dao;
        this.checkoutItemService = checkoutItemService;
        this.productService = productService;
    }

    @Override
    protected JpaRepository<CheckoutEntity, Long> getDao() {
        return dao;
    }

    @Override
    @ValidableMethod
    public CheckoutEntity create(CheckoutEntity checkout) {
        reserveProducts(checkout.getItems());
        checkout.setStatus(CheckoutStatus.PENDING);
        List<CheckoutItemEntity> items = new ArrayList<>();
        for (CheckoutItemEntity checkoutItemEntity : checkout.getItems()) {
            checkoutItemEntity.setCheckout(checkout);
            items.add(checkoutItemService.create(checkoutItemEntity));
        }
        checkoutItemService.create(items);
        return super.create(checkout);
    }

    private void reserveProducts(List<CheckoutItemEntity> items) {
        for (CheckoutItemEntity item : items) {
            ProductEntity product = productService.getOneById(item.getProduct().getId());
            manageStock(product.getStock(), item.getQuantity() + product.getStock(), product.getId());
        }
    }

    private void returnProducts(CheckoutItemEntity item) {
        manageStock(item.getProduct().getStock(), item.getProduct().getStock() - item.getQuantity(), item.getProduct().getId());
    }

    private void addStock(Long productId, int quantity) {
        ProductEntity product = productService.getOneById(productId);
        productService.modifyStock(product.getId(), product.getStock() + quantity);
    }

    private void removeStock(Long productId, int quantity) {
        ProductEntity product = productService.getOneById(productId);
        productService.modifyStock(product.getId(), product.getStock() - quantity);
    }

    public CheckoutEntity addItems(Long id, List<CheckoutItemEntity> checkoutItems) {
        CheckoutEntity checkout = getOneById(id);

        List<CheckoutItemEntity> items = checkout.getItems();
        items.addAll(checkoutItems);
        checkout.setItems(items);
        reserveProducts(checkout.getItems());
        log.info(checkout.toString());
        return update(id, checkout);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeItem(Long id, Long checkoutItemId) {
        CheckoutEntity checkout = getOneById(id);
        CheckoutItemEntity checkoutItem = checkoutItemService.getOneById(checkoutItemId);
        ServicePreconditions.checkExpression(checkout.getItems().contains(checkoutItem), "Item not found in this checkout");
        checkoutItemService.delete(checkoutItemId);
        returnProducts(checkoutItem);
        if (checkout.getItems().size() == 1) {
            dao.delete(checkout);
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void cleanCart(Long id) {
        CheckoutEntity checkout = getOneById(id);
        for (CheckoutItemEntity item : checkout.getItems()) {
            removeItem(id, item.getId());
        }
    }

    /**
     * Modifies the stock of a product based on the current and new quantities.
     *
     * @param currentQuantity The current quantity of the product in stock.
     * @param newQuantity     The new quantity to set for the product.
     * @param productId       The ID of the product.
     */
    private void manageStock(int currentQuantity, int newQuantity, Long productId) {
        int difference = currentQuantity - newQuantity;
        if (difference > 0) {
            addStock(productId, difference);
        } else {
            removeStock(productId, Math.abs(difference));
        }
    }

    public void modifyItemQuantity(Long id, Long itemId, int quantity) {
        CheckoutEntity checkout = getOneById(id);
        CheckoutItemEntity item = checkoutItemService.getOneById(itemId);
        int currentQuantity = item.getQuantity();

        ServicePreconditions.checkExpression(checkout.getItems().contains(item), "Item not found in this checkout");
        item.setQuantity(quantity);
        manageStock(currentQuantity, quantity, item.getProduct().getId());
        checkoutItemService.update(itemId, item);
    }

    public CheckoutEntity addPreferredAddress(Long id, AddressEntity address) {
        CheckoutEntity checkout = getOneById(id);
        checkout.setAddress(address);
        return update(id, checkout);
    }

    public void setStatusToCompleted(Long id) {
        CheckoutEntity checkout = getOneById(id);
        checkout.setStatus(CheckoutStatus.COMPLETED);
        dao.save(checkout);
    }

    public CheckoutEntity addPreferredPaymentMethod(Long id, PaymentMethodEntity paymentMethod) {
        CheckoutEntity checkout = getOneById(id);
        checkout.setPaymentMethod(paymentMethod);
        return update(id, checkout);
    }

    public Page<CheckoutItemEntity> getAllItemsPaginatedAndSorted(Long id, int page, int size, String sortBy, String sortOrder) {
        return checkoutItemService.getByCheckoutIdPaginatedAndSorted(id, page, size, sortBy, sortOrder);
    }

    @Override
    public Page<CheckoutEntity> getAllPaginatedAndSortedByUserId(String userId, int page, int size, String sortBy, String sortOrder) {
        return dao.findAllByUserId(userId, PageRequest.of(page, size, createSort(sortBy, sortOrder)));
    }
}
