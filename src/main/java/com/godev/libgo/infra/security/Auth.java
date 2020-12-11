package com.godev.libgo.infra.security;

import com.godev.libgo.domain.commons.exception.UnexpectedStateException;
import com.godev.libgo.domain.commons.security.exception.AuthorizationException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class Auth {

    @Nullable
    private final UUID userId;

    @Getter
    @NonNull
    private final BaseUserRole userRole;

    @NonNull private final List<BaseAuthority> authorities;

    public UUID getUserId() {
        if (userId == null) {
            throw UnexpectedStateException.because("User id is requested from Auth but is absent");
        }
        return userId;
    }

    public boolean hasUserId(@NonNull UUID id) {
        return getUserId().equals(id);
    }

    public boolean hasAuthority(@NonNull BaseAuthority authority) {
        return authorities.contains(authority);
    }

    public void requireAuthority(@NonNull BaseAuthority authority) {
        if (!hasAuthority(authority)) {
            throw AuthorizationException.noAuthority(authority);
        }
    }
}
