package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Page<PaymentEntity> findByUserId(String userId, Pageable pageable);
}
