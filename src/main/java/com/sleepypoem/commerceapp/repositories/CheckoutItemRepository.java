package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutItemRepository extends JpaRepository<CheckoutItemEntity, Long> {
    @Modifying
    @Query("update CheckoutItemEntity c set c.quantity = :quantity where c.id = :id")
    void updateQuantityOfItem(@Param("id") Long id, @Param("quantity") int quantity);
}
