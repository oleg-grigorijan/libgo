package com.godev.libgo.domain.user.persistence;

import com.godev.libgo.domain.commons.model.Email;
import com.godev.libgo.domain.commons.persistence.BaseJdbcRepository;
import com.godev.libgo.domain.user.model.IdentityDocument;
import com.godev.libgo.domain.user.model.IdentityDocumentType;
import com.godev.libgo.domain.user.model.Reader;
import com.godev.libgo.infra.persistence.JdbcConnectionPool;
import lombok.NonNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.godev.libgo.domain.commons.persistence.PersistenceUtils.toBytes;
import static com.godev.libgo.domain.commons.persistence.PersistenceUtils.toUuid;
import static java.lang.String.join;

public class ReaderJdbcRepository extends BaseJdbcRepository<Reader> implements ReaderRepository {

    private static final String TABLE = "reader";
    private static final String ID = "id";
    private static final String FULL_NAME = "full_name";
    private static final String EMAIL = "email";
    private static final String IDENTITY_DOCUMENT_TYPE = "identity_document_type";
    private static final String IDENTITY_DOCUMENT_ID = "identity_document_id";
    private static final String IS_IDENTITY_CONFIRMED = "is_identity_confirmed";

    public ReaderJdbcRepository(@NonNull JdbcConnectionPool pool) {
        super(pool, TABLE);
    }

    @Override
    protected Reader map(ResultSet result) throws SQLException {
        return new Reader(
                toUuid(result.getBytes(ID)),
                result.getString(FULL_NAME),
                Email.of(result.getString(EMAIL)),
                IdentityDocument.of(
                        IdentityDocumentType.valueOf(result.getString(IDENTITY_DOCUMENT_TYPE)),
                        result.getString(IDENTITY_DOCUMENT_ID)
                ),
                result.getBoolean(IS_IDENTITY_CONFIRMED)
        );
    }

    @Override
    public void create(@NonNull Reader reader) {
        pool.withConnection(connection -> {
            String sql = "insert into " + TABLE
                    + "(" + join(", ", ID, FULL_NAME, EMAIL, IDENTITY_DOCUMENT_TYPE, IDENTITY_DOCUMENT_ID, IS_IDENTITY_CONFIRMED) + ") "
                    + " values (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBytes(1, toBytes(reader.getId()));
                stmt.setString(2, reader.getFullName());
                stmt.setString(3, reader.getEmail().getAddress());
                stmt.setString(4, reader.getIdentityDocument().getType().name());
                stmt.setString(5, reader.getIdentityDocument().getIdentity());
                stmt.setBoolean(6, reader.isIdentityConfirmed());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public void update(@NonNull Reader reader) {
        pool.withConnection(connection -> {
            String sql = "update " + TABLE
                    + " set " + FULL_NAME + " = ?, "
                    + IDENTITY_DOCUMENT_TYPE + " = ?, "
                    + IDENTITY_DOCUMENT_ID + " = ?, "
                    + IS_IDENTITY_CONFIRMED + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, reader.getFullName());
                stmt.setString(2, reader.getIdentityDocument().getType().name());
                stmt.setString(3, reader.getIdentityDocument().getIdentity());
                stmt.setBoolean(4, reader.isIdentityConfirmed());
            }
        });
    }

    @Override
    public Optional<Reader> findByEmail(@NonNull Email email) {
        return pool.withConnectionGet(connection -> {
            String sql = "select * from " + TABLE
                    + " where " + EMAIL + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, email.getAddress());
                ResultSet result = stmt.executeQuery();
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        });
    }

    @Override
    public boolean existsByEmail(@NonNull Email email) {
        return pool.withConnectionGet(connection -> {
            String sql = "select count(*) from " + TABLE
                    + " where " + EMAIL + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, email.getAddress());
                ResultSet result = stmt.executeQuery();
                result.next();
                int count = result.getInt(1);
                return count > 0;
            }
        });
    }
}
