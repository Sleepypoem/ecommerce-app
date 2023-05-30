package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    Page<AddressEntity> findByUserId(String userId, Pageable pageable);


}
