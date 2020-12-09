package com.godev.libgo.domain.commons.security;

import com.godev.libgo.domain.commons.exception.UnexpectedStateException;
import com.godev.libgo.domain.commons.security.exception.AuthorizationException;
import com.godev.libgo.domain.user.model.UserRole;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class Auth {

    private final UUID userId;
    @NonNull private final UserRole userRole;
    @NonNull private final List<Authority> authorities;

    public UUID getUserId() {
        if (userId == null) {
            throw new UnexpectedStateException("");
        }
        return userId;
    }

    public boolean verifyUserId(@NonNull UUID id) {
        return userRole == UserRole.SUPER || getUserId().equals(id);
    }

    public void requireUserId(@NonNull UUID id) {
        if (!verifyUserId(id)) {
            throw AuthorizationException.userAuthRequired(id, userId);
        }
    }

    public boolean hasAuthority(@NonNull Authority authority) {
        return userRole == UserRole.SUPER || authorities.contains(authority);
    }

    public void requireAuthority(@NonNull Authority authority) {
        if (!hasAuthority(authority)) {
            throw AuthorizationException.noAuthority(authority);
        }
    }
}
