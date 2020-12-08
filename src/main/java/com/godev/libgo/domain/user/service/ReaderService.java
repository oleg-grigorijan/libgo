package com.godev.libgo.domain.user.service;

import com.godev.libgo.domain.user.model.Reader;
import com.godev.libgo.domain.user.model.ReaderRegistrationRequest;

import java.util.UUID;

public interface ReaderService {

    Reader getById(UUID id);

    Reader getByEmail(String email);

    Reader register(ReaderRegistrationRequest request);

    void confirmIdentity(UUID readerId);
}
