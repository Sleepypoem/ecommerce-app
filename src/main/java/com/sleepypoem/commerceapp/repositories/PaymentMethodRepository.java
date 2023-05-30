package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {
    Page<PaymentMethodEntity> findByUserId(String userId, Pageable pageable);
}
