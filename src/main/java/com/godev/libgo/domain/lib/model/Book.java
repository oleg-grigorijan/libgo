package com.godev.libgo.domain.lib.model;

import com.godev.libgo.domain.commons.model.DomainEntity;
import com.godev.libgo.domain.commons.model.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Book implements DomainEntity {

    private final UUID id;
    private final String title;
    private final String originalTitle;
    private final Language language;
    private final List<ProductAuthor> authors;
    private final String description;
}
