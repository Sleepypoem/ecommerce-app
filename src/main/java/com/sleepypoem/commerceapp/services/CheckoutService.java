package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.annotations.ValidableMethod;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
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
        reserveProductsInItemList(checkout.getItems());
        List<CheckoutItemEntity> items = checkout.getItems().stream()
                .map(item -> {
                    item.setCheckout(checkout);
                    return item;
                }).toList();
        checkoutItemService.create(items);
        checkout.setStatus(CheckoutStatus.PENDING);
        return super.create(checkout);
    }

    @Override
    public boolean delete(CheckoutEntity checkout) {
        checkout.getItems().forEach(item -> productService.increaseStock(item.getProduct().getId(), item.getQuantity()));
        return super.delete(checkout);
    }

    private void reserveProductsInItemList(List<CheckoutItemEntity> items) {
        items.forEach(item -> productService.decreaseStock(item.getProduct().getId(), item.getQuantity()));
    }

    public void addItem(Long id, CheckoutItemEntity checkoutItem) {
        CheckoutEntity checkout = getOneById(id);
        checkoutItem.setCheckout(checkout);
        productService.decreaseStock(checkoutItem.getProduct().getId(), checkoutItem.getQuantity());
        checkoutItemService.create(checkoutItem);
    }

    public CheckoutEntity addItems(Long id, List<CheckoutItemEntity> checkoutItems) {
        checkoutItems.forEach(item -> addItem(id, item));
        return getOneById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeItem(Long checkoutId, Long checkoutItemId) {
        CheckoutEntity checkout = getOneById(checkoutId);
        CheckoutItemEntity checkoutItem = checkoutItemService.getOneById(checkoutItemId);
        ServicePreconditions.checkExpression(checkout.getItems().contains(checkoutItem), "Item not found in this checkout");
        checkoutItemService.deleteById(checkoutItemId);
        productService.increaseStock(checkoutItem.getProduct().getId(), checkoutItem.getQuantity());
        checkout.getItems().remove(checkoutItem);
        log.info("Item removed from checkout. Cart size: {}", checkout.getItems().size());
        if (checkout.getItems().isEmpty()) {
            log.info("Last item in checkout, deleting checkout");
            dao.delete(checkout);
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
            productService.increaseStock(productId, difference);
        } else {
            productService.decreaseStock(productId, Math.abs(difference));
        }
    }

    public void modifyItemQuantity(Long id, Long itemId, int quantity) {
        CheckoutEntity checkout = getOneById(id);
        CheckoutItemEntity item = checkoutItemService.getOneById(itemId);
        int currentQuantity = item.getQuantity();

        ServicePreconditions.checkExpression(checkout.getItems().contains(item), "Item not found in this checkout");
        checkoutItemService.modifyQuantity(itemId, quantity);
        manageStock(currentQuantity, quantity, item.getProduct().getId());
    }

    public CheckoutEntity addPreferredAddress(Long id, AddressEntity address) {
        CheckoutEntity checkout = getOneById(id);
        checkout.setAddress(address);
        return update(id, checkout);
    }

    public void setStatusToCompleted(Long id) {
        CheckoutEntity checkout = getOneById(id);
        checkout.setStatus(CheckoutStatus.COMPLETED);
        update(id, checkout);
    }

    public void setStatusToCanceled(Long id) {
        CheckoutEntity checkout = getOneById(id);
        checkout.setStatus(CheckoutStatus.CANCELLED);
        for (CheckoutItemEntity checkoutItemEntity : checkout.getItems()) {
            productService.increaseStock(checkoutItemEntity.getProduct().getId(), checkoutItemEntity.getQuantity());
        }
        update(id, checkout);
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
