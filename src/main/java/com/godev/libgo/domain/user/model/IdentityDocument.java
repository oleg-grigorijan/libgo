package com.godev.libgo.domain.user.model;

import com.godev.libgo.domain.commons.model.DomainValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class IdentityDocument implements DomainValue {

    private final IdentityDocumentType type;
    private final String identity;
}
