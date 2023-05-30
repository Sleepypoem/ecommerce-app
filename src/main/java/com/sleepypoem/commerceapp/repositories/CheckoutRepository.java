package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRepository extends JpaRepository<CheckoutEntity, Long> {
    Page<CheckoutEntity> findByUserId(String userId, Pageable pageable);
}
