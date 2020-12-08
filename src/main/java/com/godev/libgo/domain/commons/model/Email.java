package com.godev.libgo.domain.commons.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.validator.routines.EmailValidator;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Email implements DomainValue {

    private final String address;

    public static Email of(String address) {
        if (!EmailValidator.getInstance().isValid(address)) throw new RuntimeException();
        return new Email(address);
    }
}
