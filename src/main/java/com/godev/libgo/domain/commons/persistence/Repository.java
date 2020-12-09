package com.godev.libgo.domain.commons.persistence;

import com.godev.libgo.domain.commons.model.DomainEntity;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<E extends DomainEntity> {

    List<E> getAll();

    Optional<E> findById(@NonNull UUID id);

    void create(@NonNull E entity);

    void update(@NonNull E entity);

    void delete(@NonNull UUID id);

    default void delete(@NonNull E entity) {
        delete(entity.getId());
    }
}
