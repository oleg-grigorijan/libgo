package com.godev.libgo.domain.user.model;

import com.godev.libgo.domain.commons.model.Email;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Reader implements User {

    private final UUID id;
    private final String fullName;
    private final Email email;
    private IdentityDocument identityDocument;
    private boolean identityConfirmed;

    public static Reader forCreation(String fullName, Email email, IdentityDocument identityDocument) {
        return new Reader(UUID.randomUUID(), fullName, email, identityDocument, false);
    }

    @Override
    public UserRole getRole() {
        return UserRole.READER;
    }

    public void updateIdentityDocument(IdentityDocument identityDocument) {
        this.identityDocument = identityDocument;
        identityConfirmed = false;
    }

    public void confirmIdentity() {
        identityConfirmed = true;
    }
}
