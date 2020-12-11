package com.godev.libgo.infra.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DatabaseProperties {

    private final String url;
    private final String user;
    private final String password;
}
