package com.godev.libgo.domain.commons.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Locale;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Language implements DomainValue {

    private static final Set<String> codes = stream(Locale.getISOLanguages()).collect(toSet());

    @NonNull private final String code;

    public static Language of(@NonNull String code) {
        if (!codes.contains(code)) throw new RuntimeException();
        return new Language(code);
    }
}
