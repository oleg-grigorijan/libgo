package com.godev.libgo.infra.persistence;

import java.sql.Connection;

public interface JdbcConnectionPool {

    @FunctionalInterface
    interface Action {
        void run(Connection connection) throws Exception;
    }

    @FunctionalInterface
    interface ResultAction<T> {
        T run(Connection connection) throws Exception;
    }

    default void withConnection(Action action) {
        withConnectionGet(connection -> {
            action.run(connection);
            return null;
        });
    }

    <T> T withConnectionGet(ResultAction<T> action);
}
