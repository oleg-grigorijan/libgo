package com.godev.libgo.domain.commons.persistence;

import com.godev.libgo.infra.persistence.TxTemplate;

import java.util.function.Supplier;

public class TxTemplateMock implements TxTemplate {

    @Override
    public <T> T transactionalGet(Supplier<T> action) {
        return action.get();
    }
}
