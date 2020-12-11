package com.godev.libgo.domain.order.persistence;

import com.godev.libgo.domain.commons.persistence.BaseJdbcRepository;
import com.godev.libgo.domain.order.model.Order;
import com.godev.libgo.domain.order.model.OrderState;
import com.godev.libgo.domain.order.model.OrderType;
import com.godev.libgo.infra.persistence.JdbcConnectionPool;
import lombok.NonNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.godev.libgo.domain.commons.persistence.PersistenceUtils.toBytes;
import static com.godev.libgo.domain.commons.persistence.PersistenceUtils.toUuid;
import static java.lang.String.join;

public class OrderRepositoryImpl extends BaseJdbcRepository<Order> implements OrderRepository {

    private static final String TABLE = "order";
    private static final String ID = "id";
    private static final String LIB_ITEM_ID = "lib_item_id";
    private static final String READER_ID = "reader_id";
    private static final String INITIATOR_ID = "initiator_id";
    private static final String TAKEN_FROM_DATE = "taken_from_date";
    private static final String TAKEN_TO_DATE = "taken_to_date";
    private static final String TYPE = "type";
    private static final String STATE = "state";

    public OrderRepositoryImpl(@NonNull JdbcConnectionPool pool) {
        super(pool, TABLE);
    }

    @Override
    protected Order map(ResultSet result) throws SQLException {
        return new Order(
                toUuid(result.getBytes(ID)),
                toUuid(result.getBytes(LIB_ITEM_ID)),
                toUuid(result.getBytes(READER_ID)),
                toUuid(result.getBytes(INITIATOR_ID)),
                result.getDate(TAKEN_FROM_DATE).toLocalDate(),
                result.getDate(TAKEN_TO_DATE).toLocalDate(),
                OrderType.valueOf(result.getString(TYPE)),
                OrderState.valueOf(result.getString(STATE))
        );
    }

    @Override
    public void create(@NonNull Order order) {
        pool.withConnection(connection -> {
            String sql = "insert into " + TABLE +
                    "(" + join(", ", ID, LIB_ITEM_ID, READER_ID, INITIATOR_ID, TAKEN_FROM_DATE, TAKEN_TO_DATE, TYPE, STATE) + ") " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBytes(1, toBytes(order.getId()));
                stmt.setBytes(2, toBytes(order.getLibItemId()));
                stmt.setBytes(3, toBytes(order.getReaderId()));
                stmt.setBytes(4, toBytes(order.getInitiatorId()));
                stmt.setDate(5, Date.valueOf(order.getTakenFromDate()));
                stmt.setDate(6, Date.valueOf(order.getTakenToDate()));
                stmt.setString(7, order.getType().name());
                stmt.setString(8, order.getState().name());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public void update(@NonNull Order order) {
        pool.withConnection(connection -> {
            String sql = "update " + TABLE
                    + " set " + STATE + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, order.getState().name());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public List<Order> findByReaderId(@NonNull UUID readerId) {
        return pool.withConnectionGet(connection -> {
            String sql = "select * from " + TABLE
                    + " where " + READER_ID + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBytes(1, toBytes(readerId));
                ResultSet result = stmt.executeQuery();
                return mapAll(result);
            }
        });
    }

    @Override
    public List<Order> findAllByLibItemIdAndState(@NonNull UUID libItemId, @NonNull OrderState state) {
        return pool.withConnectionGet(connection -> {
            String sql = "select * from " + TABLE
                    + " where " + LIB_ITEM_ID + " = ?"
                    + "   and " + STATE + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBytes(1, toBytes(libItemId));
                stmt.setString(2, state.name());
                ResultSet result = stmt.executeQuery();
                return mapAll(result);
            }
        });
    }

    @Override
    public List<Order> findAllByTakenFromDateIsOrTakenToDateIsOrStateIs(LocalDate date, OrderState state) {
        return pool.withConnectionGet(connection -> {
            String sql = "select * from " + TABLE
                    + " where " + TAKEN_FROM_DATE + " = ?"
                    + "    or " + TAKEN_TO_DATE + " = ?"
                    + "    or " + STATE + " = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(date));
                stmt.setDate(2, Date.valueOf(date));
                stmt.setString(3, state.name());
                ResultSet result = stmt.executeQuery();
                return mapAll(result);
            }
        });
    }
}
