package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.StripeUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeUserRepository extends JpaRepository<StripeUserEntity, String> {

    Optional<StripeUserEntity> findByUserId(String userId);
}
