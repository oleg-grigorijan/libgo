package com.godev.libgo.domain.commons.persistence;

import com.godev.libgo.domain.commons.model.DomainEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.godev.libgo.domain.commons.persistence.PersistenceUtils.toBytes;

@RequiredArgsConstructor
public abstract class BaseJdbcRepository<E extends DomainEntity> implements Repository<E> {

    @NonNull protected final JdbcConnectionPool pool;
    @NonNull private final String table;

    protected abstract E map(ResultSet result) throws SQLException;

    protected List<E> mapAll(ResultSet result) throws SQLException {
        List<E> entities = new ArrayList<>();
        while (result.next()) {
            entities.add(map(result));
        }
        return entities;
    }

    @Override
    public List<E> getAll() {
        return pool.withConnectionGet(connection -> {
            String sql = "select * from " + table;
            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(sql);
                return mapAll(result);
            }
        });
    }

    @Override
    public Optional<E> findById(@NonNull UUID id) {
        return pool.withConnectionGet(connection -> {
            String sql = "select * from " + table + " where id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBytes(1, toBytes(id));
                ResultSet result = stmt.executeQuery();
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        });
    }

    @Override
    public void delete(@NonNull UUID id) {
        pool.withConnection(connection -> {
            String sql = "delete from " + table + " where id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBytes(1, toBytes(id));
                stmt.executeUpdate();
            }
        });
    }
}
