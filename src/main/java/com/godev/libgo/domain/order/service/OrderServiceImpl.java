package com.godev.libgo.domain.order.service;

import com.godev.libgo.domain.commons.exception.NotImplementedException;
import com.godev.libgo.domain.commons.persistence.TxTemplate;
import com.godev.libgo.domain.commons.security.Auth;
import com.godev.libgo.domain.commons.security.Authority;
import com.godev.libgo.domain.commons.security.SecurityContext;
import com.godev.libgo.domain.order.model.Order;
import com.godev.libgo.domain.order.model.OrderCreateRequest;
import com.godev.libgo.domain.order.persistence.OrderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @NonNull private final OrderRepository repository;
    @NonNull private final SecurityContext security;
    @NonNull private final TxTemplate tx;
    @NonNull private final Clock clock;

    private Order getById(UUID id) {
        return tx.transactionalGet(() -> repository.findById(id).orElseThrow());
    }

    @Override
    public Optional<Order> findByLibItem(UUID libItemId) {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.VIEW_ANY_ORDER);


        throw new NotImplementedException();
    }

    @Override
    public List<Order> getAllActualForToday() {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.VIEW_ANY_ORDER);

        LocalDate today = LocalDate.now(clock);

        throw new NotImplementedException();
    }

    @Override
    public List<Order> getAllByReader(UUID readerId) {
        return tx.transactionalGet(() -> repository.findByReaderId(readerId));
    }

    @Override
    public Order make(OrderCreateRequest request) {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.MAKE_ORDER_FOR_SELF);

        UUID selfId = auth.getUserId();
        return doCreate(request, selfId, selfId);
    }

    @Override
    public Order makeForReader(OrderCreateRequest request, UUID readerId) {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.MAKE_ORDER_FOR_OTHER);

        UUID initiatorId = auth.getUserId();
        return doCreate(request, readerId, initiatorId);
    }

    private Order doCreate(OrderCreateRequest request, UUID readerId, UUID initiatorId) {
        return tx.transactionalGet(() -> {

            Order order = Order.forCreation(
                    request.getLibItemId(),
                    readerId,
                    initiatorId,
                    request.getTakenFromDate(),
                    request.getTakenToDate(),
                    request.getType()
            );
            repository.create(order);
            return order;
        });
    }

    @Override
    public void markAccepted(UUID orderId) {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.PROCESS_ORDER);

        tx.transactional(() -> {
            Order order = getById(orderId);
            order.markAccepted();

            repository.update(order);
        });
    }

    @Override
    public void markDelivered(UUID orderId) {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.PROCESS_ORDER);

        tx.transactional(() -> {
            Order order = getById(orderId);
            order.markDelivered();

            repository.update(order);
        });
    }

    @Override
    public void markReturned(UUID orderId) {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.PROCESS_ORDER);

        tx.transactional(() -> {
            Order order = getById(orderId);
            order.markReturned();

            repository.update(order);
        });
    }

    @Override
    public void cancel(UUID orderId) {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.CANCEL_ORDER);

        tx.transactional(() -> {
            Order order = getById(orderId);
            order.markCancelled();

            repository.update(order);
        });
    }
}