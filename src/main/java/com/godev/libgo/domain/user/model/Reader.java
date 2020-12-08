package com.godev.libgo.domain.user.model;

import com.godev.libgo.domain.commons.model.Email;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Reader implements User {

    private final UUID id;
    private final String fullName;
    private final Email email;
    private IdentityDocument identityDocument;
    private boolean identityConfirmed;


    @Override
    public UserRole getRole() {
        return UserRole.READER;
    }

    public void updateIdentityDocument(IdentityDocument identityDocument) {
        this.identityDocument = identityDocument;
        identityConfirmed = false;
    }

    public void identityDocument() {
        identityConfirmed = true;
    }
}
