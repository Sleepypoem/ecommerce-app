package com.sleepypoem.commerceapp.domain.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;

import java.time.LocalDateTime;
import java.util.Objects;

public class AbstractDto<ID> implements IDto<ID> {

    protected ID id;

    protected LocalDateTime createdAt;


    protected LocalDateTime updatedAt;
    @Override
    public ID getId() {
        return null;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDto<?> that = (AbstractDto<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
