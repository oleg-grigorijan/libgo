package com.godev.libgo.domain.user.service;

import com.godev.libgo.domain.commons.model.Email;
import com.godev.libgo.domain.user.model.Reader;
import com.godev.libgo.domain.user.model.ReaderRegistrationRequest;
import lombok.NonNull;

import java.util.UUID;

public interface ReaderService {

    Reader getById(@NonNull UUID id);

    Reader getByEmail(@NonNull Email email);

    Reader register(@NonNull ReaderRegistrationRequest request);

    void confirmIdentity(@NonNull UUID readerId);
}
