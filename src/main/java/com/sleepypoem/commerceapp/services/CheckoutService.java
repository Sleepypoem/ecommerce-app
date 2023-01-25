package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.dto.CheckoutItemDto;
import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutItemMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutMapper;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyResourceNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;
import com.sleepypoem.commerceapp.repositories.CheckoutRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import com.sleepypoem.commerceapp.services.validators.Validator;
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
    IValidator<CheckoutEntity> validator;

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
        return validator;
    }

    @Override
    public CheckoutDto create(CheckoutEntity checkout) throws MyEntityNotFoundException, MyValidationException {
        Validator.validate(validator, checkout);
        reserveProducts(checkout);
        checkout.setStatus(CheckoutStatus.PENDING);
        return super.create(checkout);
    }

    private void reserveProducts(CheckoutEntity checkout) {
        for (CheckoutItemEntity item : checkout.getItems()) {
            ProductDto product = productService.getOneById(item.getProduct().getId());

            productService.modifyStock(product.getId(), product.getStock() - item.getQuantity());
        }
    }

    private void returnProducts(CheckoutItemEntity item) {
        productService.modifyStock(item.getProduct().getId(), item.getProduct().getStock() + item.getQuantity());
    }

    public List<CheckoutEntity> getByUserId(String userId) {
        return dao.findByUserId(userId);
    }

    public CheckoutDto addItems(Long id, List<CheckoutItemEntity> checkoutItems) throws Exception {
        CheckoutEntity checkout = mapper.convertToEntity(this.getOneById(id));

        List<CheckoutItemEntity> items = checkout.getItems();
        items.addAll(checkoutItems);
        checkout.setItems(items);
        reserveProducts(checkout);
        return update(id, checkout);
    }

    public CheckoutDto removeItem(Long id, Long checkoutItemId) {
        CheckoutEntity checkout = mapper.convertToEntity(this.getOneById(id));
        CheckoutItemDto checkoutItemDto = checkoutItemService.getOneById(checkoutItemId);

        CheckoutItemEntity checkoutItem = checkoutItemMapper.convertToEntity(checkoutItemDto);

        List<CheckoutItemEntity> items = checkout.getItems();
        items.remove(checkoutItem);
        checkout.setItems(items);
        returnProducts(checkoutItem);
        CheckoutDto result = getMapper().convertToDto(dao.save(checkout));

        if (result.getItems().isEmpty()) {
            getDao().deleteById(id);
            return null;
        }
        return result;
    }

    public CheckoutDto modifyItemQuantity(Long id, Long itemId, int quantity) {
        CheckoutEntity checkout = mapper.convertToEntity(this.getOneById(id));
        CheckoutItemEntity item = checkoutItemMapper.convertToEntity(checkoutItemService.getOneById(itemId));

        CheckoutItemDto modifiedItem = checkoutItemService.modifyQuantity(itemId, quantity);
        List<CheckoutItemEntity> items = checkout.getItems();
        items = items.stream().filter(i -> !i.equals(item)).collect(Collectors.toList());
        items.add(checkoutItemMapper.convertToEntity(modifiedItem));

        return getMapper().convertToDto(dao.save(checkout));
    }

    public CheckoutDto addPreferredAddress(Long id, AddressEntity address) {
        CheckoutEntity checkout = mapper.convertToEntity(this.getOneById(id));

        checkout.setAddress(address);
        return getMapper().convertToDto(dao.save(checkout));
    }

    public void setStatusToCompleted(Long id) {
        CheckoutEntity checkout = mapper.convertToEntity(this.getOneById(id));

        checkout.setStatus(CheckoutStatus.COMPLETED);
        dao.save(checkout);
    }

    public CheckoutDto addPreferredPaymentMethod(Long id, PaymentMethodEntity paymentMethod) {
        CheckoutEntity checkout = mapper.convertToEntity(this.getOneById(id));

        checkout.setPaymentMethod(paymentMethod);
        return getMapper().convertToDto(dao.save(checkout));
    }
}
