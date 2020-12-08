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
public class Employee implements User {

    private final UUID id;
    private final String fullName;
    private final Email email;
    private final UserRole role;
}
