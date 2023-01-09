package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckoutRepository extends JpaRepository<CheckoutEntity, Long> {
    public Optional<CheckoutDto> findByUserId(String userId);
}
