package com.godev.libgo.domain.user.model;

import com.godev.libgo.domain.commons.model.DomainEntity;
import com.godev.libgo.domain.commons.model.Email;

public interface User extends DomainEntity {

    String getFullName();

    Email getEmail();

    UserRole getRole();
}
