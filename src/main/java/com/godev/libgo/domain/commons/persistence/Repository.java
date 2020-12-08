package com.godev.libgo.domain.commons.persistence;

import com.godev.libgo.domain.commons.model.DomainEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<E extends DomainEntity> {

    List<E> getAll();

    Optional<E> findById(UUID id);

    void create(E entity);

    void update(E entity);

    void delete(UUID id);

    default void delete(E entity) {
        delete(entity.getId());
    }
}
