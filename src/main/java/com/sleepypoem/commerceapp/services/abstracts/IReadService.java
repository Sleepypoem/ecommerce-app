package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;

import java.util.List;

public interface IReadService<T extends IDto> {

    T getOneById(Long id);

    List<T> getAll();
}
