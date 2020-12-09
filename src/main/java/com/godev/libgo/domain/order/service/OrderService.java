package com.godev.libgo.domain.order.service;

import com.godev.libgo.domain.order.model.Order;
import com.godev.libgo.domain.order.model.OrderCreateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    Optional<Order> findByLibItem(UUID libItemId);

    List<Order> getAllActualForToday();

    List<Order> getAllByReader(UUID readerId);

    Order make(OrderCreateRequest request);

    Order makeForReader(OrderCreateRequest request, UUID readerId);

    void markAccepted(UUID orderId);

    void markDelivered(UUID orderId);

    void markReturned(UUID orderId);

    void cancel(UUID orderId);
}
