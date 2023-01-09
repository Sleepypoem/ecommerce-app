package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {
    List<PaymentMethodEntity> findByUserId(String userId);
}
