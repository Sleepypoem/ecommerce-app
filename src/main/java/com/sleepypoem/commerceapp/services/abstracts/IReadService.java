package com.sleepypoem.commerceapp.services.abstracts;

import java.util.List;

public interface IReadService<E> {

    E getOneById(Long id);

    List<E> getAll();
}
