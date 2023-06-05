package com.sleepypoem.commerceapp.domain.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@SuperBuilder
public class AbstractEntity<ID> implements IEntity<ID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected ID id;

    @JsonIgnore
    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    protected AbstractEntity() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public ID getId() {
        return id;
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
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
