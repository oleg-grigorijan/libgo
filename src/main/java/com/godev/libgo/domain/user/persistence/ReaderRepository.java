package com.godev.libgo.domain.user.persistence;

import com.godev.libgo.domain.commons.model.Email;
import com.godev.libgo.domain.commons.persistence.Repository;
import com.godev.libgo.domain.user.model.Reader;
import lombok.NonNull;

import java.util.Optional;

public interface ReaderRepository extends Repository<Reader> {

    Optional<Reader> findByEmail(Email email);

    boolean existsByEmail(@NonNull Email email);
}
