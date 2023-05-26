package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRepository extends JpaRepository<CheckoutEntity, Long> {
    List<CheckoutEntity> findByUserId(String userId);
}
