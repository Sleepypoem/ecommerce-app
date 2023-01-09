package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;

import java.util.List;
import java.util.Optional;

public interface IReadService<T extends IDto> {

    Optional<T> getOneById(Long id);

    List<T> getAll();
}
