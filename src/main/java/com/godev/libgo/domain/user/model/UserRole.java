package com.godev.libgo.domain.user.model;

import com.godev.libgo.infra.security.BaseUserRole;

public enum UserRole implements BaseUserRole {

    GUEST,
    LIBRARIAN,
    HR,
    READER,
    SUPER;

    public boolean isSuper() {
        return this == SUPER;
    }
}
