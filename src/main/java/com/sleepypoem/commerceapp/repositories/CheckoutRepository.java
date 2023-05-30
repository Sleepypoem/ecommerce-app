package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutRepository extends JpaRepository<CheckoutEntity, Long> {
    Page<CheckoutEntity> findAllByUserId(String userId, Pageable pageable);
}
