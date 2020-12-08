package com.godev.libgo.domain.lib.model;

import com.godev.libgo.domain.commons.model.DomainEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ProductAuthor implements DomainEntity {

    private final UUID id;
    private String fullName;
}
