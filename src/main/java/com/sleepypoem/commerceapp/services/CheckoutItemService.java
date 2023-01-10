package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.CheckoutItemDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutItemMapper;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.CheckoutItemRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CheckoutItemService extends AbstractService<CheckoutItemDto, CheckoutItemEntity> {

    @Autowired
    IValidator<CheckoutItemEntity> validateCheckoutItem;

    @Autowired
    CheckoutItemRepository dao;

    @Autowired
    CheckoutItemMapper mapper;

    @Override
    protected JpaRepository<CheckoutItemEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected BaseMapper<CheckoutItemEntity, CheckoutItemDto> getMapper() {
        return mapper;
    }

    @Override
    protected IValidator<CheckoutItemEntity> getValidator() {
        return validateCheckoutItem;
    }

    public CheckoutItemDto modifyQuantity(Long id, int quantity) {
        Optional<CheckoutItemEntity> searchedItem = dao.findById(id);
        if (searchedItem.isEmpty()) {
            throw new MyEntityNotFoundException("Item with id " + id + " not found");
        }
        CheckoutItemEntity item = searchedItem.get();
        item.setQuantity(quantity);

        return mapper.convertToDto(dao.save(item));
    }
}
