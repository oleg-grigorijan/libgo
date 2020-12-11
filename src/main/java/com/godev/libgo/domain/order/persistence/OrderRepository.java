package com.godev.libgo.domain.order.persistence;

import com.godev.libgo.domain.commons.persistence.Repository;
import com.godev.libgo.domain.order.model.Order;
import com.godev.libgo.domain.order.model.OrderState;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends Repository<Order> {

    List<Order> findByReaderId(@NonNull UUID readerId);

    List<Order> findAllByLibItemIdAndState(@NonNull UUID libItemId, @NonNull OrderState state);

    List<Order> findAllByTakenFromDateIsOrTakenToDateIsOrStateIs(LocalDate date, OrderState state);
}
