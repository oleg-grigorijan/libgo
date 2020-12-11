package com.godev.libgo.domain.commons.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.validator.routines.EmailValidator;

import static com.godev.libgo.MessageKeys.Commons.Error.BADLY_FORMATTED_EMAIL;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Email implements DomainValue {

    private final String address;

    public static Email of(@NonNull String address) {
        if (!EmailValidator.getInstance().isValid(address)) {
            throw new IllegalArgumentException(BADLY_FORMATTED_EMAIL);
        }
        return new Email(address);
    }
}
