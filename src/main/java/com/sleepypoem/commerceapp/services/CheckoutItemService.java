package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.repositories.CheckoutItemRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.impl.ValidateCheckoutItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Validable(ValidateCheckoutItem.class)
public class CheckoutItemService extends AbstractService<CheckoutItemEntity, Long> {

    private final CheckoutItemRepository dao;

    public CheckoutItemService(CheckoutItemRepository dao) {
        this.dao = dao;
    }

    @Override
    protected JpaRepository<CheckoutItemEntity, Long> getDao() {
        return dao;
    }

    public CheckoutItemEntity modifyQuantity(Long id, int quantity) {
        CheckoutItemEntity item = getOneById(id);
        item.setQuantity(quantity);

        return update(id, item);
    }

    public Page<CheckoutItemEntity> getByCheckoutIdPaginatedAndSorted(Long checkoutId, int page, int size, String sortBy, String sortOrder) {
        return dao.findByCheckoutId(checkoutId, PageRequest.of(page, size, createSort(sortBy, sortOrder)));
    }

    @Transactional
    public List<CheckoutItemEntity> create(List<CheckoutItemEntity> items) {
        List<CheckoutItemEntity> createdItems = new ArrayList<>();
        for (CheckoutItemEntity item : items) {
            createdItems.add(create(item));
        }
        return createdItems;
    }
}
