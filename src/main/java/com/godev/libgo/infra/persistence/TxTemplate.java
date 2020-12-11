package com.godev.libgo.infra.persistence;

import java.util.function.Supplier;

public interface TxTemplate {

    default void transactional(Runnable action) {
        transactionalGet(() -> {
            action.run();
            return null;
        });
    }

    <T> T transactionalGet(Supplier<T> action);
}
