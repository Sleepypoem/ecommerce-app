package com.sleepypoem.commerceapp.utils.factories.abstracts;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface SimpleFactory<T> {

    T create();

    default List<T> createList(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> create())
                .collect(Collectors.toList());
    }
}
