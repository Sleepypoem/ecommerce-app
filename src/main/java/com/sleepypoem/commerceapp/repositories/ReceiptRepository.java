package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Long> {
}
