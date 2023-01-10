package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.dto.CheckoutItemDto;
import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutItemMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutMapper;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyResourceNotFoundException;
import com.sleepypoem.commerceapp.repositories.CheckoutRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CheckoutService extends AbstractService<CheckoutDto, CheckoutEntity> {


    @Autowired
    IValidator<CheckoutEntity> validateCheckout;

    @Autowired
    CheckoutRepository dao;

    @Autowired
    CheckoutMapper mapper;

    @Autowired
    CheckoutItemService checkoutItemService;

    @Autowired
    ProductService productService;

    @Autowired
    CheckoutItemMapper checkoutItemMapper;

    @Override
    protected JpaRepository<CheckoutEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected BaseMapper<CheckoutEntity, CheckoutDto> getMapper() {
        return mapper;
    }

    @Override
    protected IValidator<CheckoutEntity> getValidator() {
        return validateCheckout;
    }

    @Override
    public CheckoutDto create(CheckoutEntity checkout) throws Exception {
        reserveProducts(checkout);
        return super.create(checkout);
    }

    private void reserveProducts(CheckoutEntity checkout) {
        for (CheckoutItemEntity item : checkout.getItems()) {
            Optional<ProductDto> searchedProduct = productService.getOneById(item.getProduct().getId());
            if (searchedProduct.isEmpty()) {
                throw new MyResourceNotFoundException("Product with id " + item.getProduct().getId() + " not found.");
            }
            ProductDto product = searchedProduct.get();
            log.info(String.valueOf(item.getQuantity()));
            productService.modifyStock(product.getId(), product.getStock() - item.getQuantity());
        }
    }

    private void returnProducts(CheckoutItemEntity item) {
        productService.modifyStock(item.getProduct().getId(), item.getProduct().getStock() + item.getQuantity());
    }

    public CheckoutEntity getByUserId(String userId) {
        Optional<CheckoutEntity> searchedCheckout = dao.findOneByUserId(userId);
        if (searchedCheckout.isEmpty()) {
            return null;
        }
        return searchedCheckout.get();
    }

    public CheckoutDto addItems(Long id, List<CheckoutItemEntity> checkoutItems) throws Exception {
        Optional<CheckoutEntity> searchedCheckout = dao.findById(id);
        log.info(String.valueOf(id));
        if (searchedCheckout.isEmpty()) {
            throw new MyEntityNotFoundException("Checkout with id " + id + " not found");
        }
        CheckoutEntity checkout = searchedCheckout.get();

        List<CheckoutItemEntity> items = checkout.getItems();
        items.addAll(checkoutItems);
        checkout.setItems(items);
        reserveProducts(checkout);
        log.info(checkout.toString());
        return update(id, checkout);
    }

    public CheckoutDto removeItem(Long id, Long checkoutItemId) {
        Optional<CheckoutEntity> searchedCheckout = dao.findById(id);
        Optional<CheckoutItemEntity> searchedCheckoutItem = checkoutItemService.getDao().findById(checkoutItemId);

        if (searchedCheckout.isEmpty() || searchedCheckoutItem.isEmpty()) {
            String message = searchedCheckout.isEmpty() ? "Checkout with id " + id : "Item with id " + checkoutItemId;
            throw new MyEntityNotFoundException(message + " not found");
        }

        CheckoutEntity checkout = searchedCheckout.get();
        CheckoutItemEntity checkoutItem = searchedCheckoutItem.get();

        List<CheckoutItemEntity> items = checkout.getItems();
        items.remove(checkoutItem);
        checkout.setItems(items);
        returnProducts(checkoutItem);
        log.info(checkout.toString());
        CheckoutDto result = getMapper().convertToDto(dao.save(checkout));

        if (result.getItems().isEmpty()) {
            getDao().deleteById(id);
            return null;
        }
        return result;
    }

    public CheckoutDto modifyItemQuantity(Long id, Long itemId, int quantity) {
        Optional<CheckoutEntity> searchedCheckout = dao.findById(id);
        Optional<CheckoutItemEntity> searchedItem = checkoutItemService.getDao().findById(itemId);

        if (searchedCheckout.isEmpty() || searchedItem.isEmpty()) {
            String message = searchedCheckout.isEmpty() ? "Checkout with id " + id : "Item with id " + itemId;
            throw new MyEntityNotFoundException(message + " not found");
        }
        CheckoutEntity checkout = searchedCheckout.get();
        CheckoutItemEntity item = searchedItem.get();

        CheckoutItemDto modifiedItem = checkoutItemService.modifyQuantity(itemId, quantity);
        List<CheckoutItemEntity> items = checkout.getItems();
        items = items.stream().filter(i -> !i.equals(item)).collect(Collectors.toList());
        items.add(checkoutItemMapper.convertToEntity(modifiedItem));

        return getMapper().convertToDto(dao.save(checkout));
    }
}
