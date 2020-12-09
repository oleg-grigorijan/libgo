package com.godev.libgo.domain.commons.persistence;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

@Slf4j
public class ThreadLocalJdbcConnectionPool implements JdbcConnectionPool, TxTemplate {

    private final BlockingQueue<Connection> connections;
    private final ThreadLocal<Connection> currentConnection;

    public ThreadLocalJdbcConnectionPool(int size, @NonNull DatabaseProperties props) {
        currentConnection = new ThreadLocal<>();
        connections = new ArrayBlockingQueue<>(size);

        try {
            for (int i = 0; i < size; i++) {
                Connection connection = DriverManager.getConnection(props.getUrl(), props.getUser(), props.getPassword());
                connection.setAutoCommit(false);
                // TODO: Create connection wrapper where Connection#close
                //  and Connection#setAutoCommit are ignored
                connections.add(connection);
            }

        } catch (SQLException e) {
            throw PersistenceException.cantConnect(e);
        }
    }

    @Override
    public <T> T withConnectionGet(ResultAction<T> action) {
        Connection connection = currentConnection.get();
        if (connection == null) throw PersistenceException.noTransactionContext();
        try {
            return action.run(connection);
        } catch (Exception e) {
            throw PersistenceException.unexpected(e);
        }
    }

    @Override
    public <T> T transactionalGet(Supplier<T> action) {
        boolean shouldReleaseConnection = false;
        Connection connection = currentConnection.get();
        if (connection == null) {
            connection = takeConnection();
            shouldReleaseConnection = true;
            currentConnection.set(connection);
        }

        try {
            T result = action.get();
            connection.commit();
            return result;

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception e1) {
                log.error("Can't rollback persistence transaction", e1);
            }
            throw PersistenceException.unexpected(e);

        } finally {
            if (shouldReleaseConnection) {
                connections.add(connection);
                currentConnection.remove();
            }
        }
    }

    private Connection takeConnection() {
        try {
            return connections.take();
        } catch (InterruptedException e) {
            throw PersistenceException.unexpected(e);
        }
    }
}
