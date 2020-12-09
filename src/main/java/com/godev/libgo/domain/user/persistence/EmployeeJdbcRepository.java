package com.godev.libgo.domain.user.persistence;

import com.godev.libgo.domain.commons.model.Email;
import com.godev.libgo.domain.commons.persistence.BaseJdbcRepository;
import com.godev.libgo.domain.commons.persistence.JdbcConnectionPool;
import com.godev.libgo.domain.user.model.Employee;
import com.godev.libgo.domain.user.model.UserRole;
import lombok.NonNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.godev.libgo.domain.commons.persistence.PersistenceUtils.toBytes;
import static com.godev.libgo.domain.commons.persistence.PersistenceUtils.toUuid;
import static java.lang.String.join;

public class EmployeeJdbcRepository extends BaseJdbcRepository<Employee> implements EmployeeRepository {

    private static final String TABLE = "employee";
    private static final String ID = "id";
    private static final String FULL_NAME = "full_name";
    private static final String EMAIL = "email";
    private static final String ROLE = "role";

    public EmployeeJdbcRepository(JdbcConnectionPool pool) {
        super(pool, TABLE);
    }

    @Override
    protected Employee map(ResultSet result) throws SQLException {
        return new Employee(
                toUuid(result.getBytes(ID)),
                result.getString(FULL_NAME),
                Email.of(result.getString(EMAIL)),
                UserRole.valueOf(result.getString(ROLE))
        );
    }

    @Override
    public void create(@NonNull Employee employee) {
        pool.withConnection(connection -> {
            String sql = "insert into " + TABLE
                    + " (" + join(", ", ID, FULL_NAME, EMAIL, ROLE) + ") "
                    + "values (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBytes(1, toBytes(employee.getId()));
                stmt.setString(2, employee.getFullName());
                stmt.setString(3, employee.getEmail().getAddress());
                stmt.setString(4, employee.getRole().name());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public void update(@NonNull Employee employee) {
        pool.withConnection(connection -> {
            String sql = "update " + TABLE
                    + " set " + FULL_NAME + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, employee.getFullName());
                stmt.executeUpdate();
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
