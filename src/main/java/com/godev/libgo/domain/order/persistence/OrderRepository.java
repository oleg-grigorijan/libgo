package com.godev.libgo.domain.order.persistence;

import com.godev.libgo.domain.commons.persistence.Repository;
import com.godev.libgo.domain.order.model.Order;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends Repository<Order> {

    List<Order> findByReaderId(@NonNull UUID readerId);
}
