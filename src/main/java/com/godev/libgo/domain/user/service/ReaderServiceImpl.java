package com.godev.libgo.domain.user.service;

import com.godev.libgo.domain.commons.exception.EntityNotFoundException;
import com.godev.libgo.domain.commons.model.Email;
import com.godev.libgo.domain.commons.persistence.TxTemplate;
import com.godev.libgo.domain.commons.security.Auth;
import com.godev.libgo.domain.commons.security.Authority;
import com.godev.libgo.domain.commons.security.SecurityContext;
import com.godev.libgo.domain.user.UserException;
import com.godev.libgo.domain.user.model.Reader;
import com.godev.libgo.domain.user.model.ReaderRegistrationRequest;
import com.godev.libgo.domain.user.persistence.ReaderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    @NonNull private final ReaderRepository repository;
    @NonNull private final SecurityContext security;
    @NonNull private final TxTemplate tx;

    @Override
    public Reader getById(@NonNull UUID id) {
        return tx.transactionalGet(() -> repository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(id)));
    }

    @Override
    public Reader getByEmail(@NonNull Email email) {
        return tx.transactionalGet(() -> {
            Reader reader = repository.findByEmail(email)
                    .orElseThrow(() -> EntityNotFoundException.byIdentity(email.getAddress()));

            Auth auth = security.getCurrentAuth();
            if (!auth.verifyUserId(reader.getId())) {
                auth.requireAuthority(Authority.VIEW_ANY_READER);
            }

            return reader;
        });
    }

    @Override
    public Reader register(@NonNull ReaderRegistrationRequest request) {
        return tx.transactionalGet(() -> {
            Email email = request.getEmail();

            if (repository.existsByEmail(email)) {
                throw UserException.alreadyExistsByEmail(email);
            }

            Reader reader = Reader.forCreation(
                    request.getFullName(),
                    request.getEmail(),
                    request.getIdentityDocument()
            );
            repository.create(reader);
            return reader;
        });
    }

    @Override
    public void confirmIdentity(@NonNull UUID readerId) {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.CONFIRM_READER_IDENTITY);

        tx.transactional(() -> {
            Reader reader = getById(readerId);
            reader.confirmIdentity();
            repository.update(reader);
        });
    }
}
