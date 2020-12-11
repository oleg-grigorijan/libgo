package com.godev.libgo.domain.order.service;

import com.godev.libgo.MessageKeys;
import com.godev.libgo.domain.commons.model.DateRange;
import com.godev.libgo.domain.commons.persistence.TxTemplateMock;
import com.godev.libgo.domain.commons.security.Auths;
import com.godev.libgo.domain.commons.security.exception.AuthorizationException;
import com.godev.libgo.domain.order.OrderException;
import com.godev.libgo.domain.order.model.Order;
import com.godev.libgo.domain.order.model.OrderCreateRequest;
import com.godev.libgo.domain.order.model.OrderState;
import com.godev.libgo.domain.order.model.OrderType;
import com.godev.libgo.domain.order.persistence.OrderRepository;
import com.godev.libgo.infra.security.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    static final UUID LIB_ITEM_ID = randomUUID();
    static final OrderType TYPE = OrderType.READING_ROOM;
    static final UUID READER_ID = randomUUID();
    static final UUID LIBRARIAN_ID = randomUUID();
    static final DateRange TAKEN_PERIOD = DateRange.ofSingleDay(LocalDate.of(2020, 12, 11));

    private static Order order(UUID id, OrderState state) {
        return new Order(
                id,
                LIB_ITEM_ID,
                READER_ID,
                READER_ID,
                TAKEN_PERIOD,
                TYPE,
                state
        );
    }

    private static Order order(OrderState state) {
        return order(randomUUID(), state);
    }

    OrderService orderService;
    @Mock OrderRepository repository;
    @Mock SecurityContext security;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(repository, security, new TxTemplateMock(), Clock.systemUTC());
    }

    @Test
    void orderShouldBeCreated() {
        var request = OrderCreateRequest.of(LIB_ITEM_ID, TYPE, TAKEN_PERIOD);

        when(security.getCurrentAuth()).thenReturn(Auths.forReader(READER_ID));

        Order order = orderService.make(request);
        assertThat(order)
                .returns(LIB_ITEM_ID, from(Order::getLibItemId))
                .returns(TYPE, from(Order::getType))
                .returns(TAKEN_PERIOD, from(Order::getTakenPeriod))
                .returns(READER_ID, from(Order::getReaderId))
                .returns(READER_ID, from(Order::getInitiatorId))
                .returns(OrderState.OPEN, from(Order::getState))
                .extracting(Order::getId).isNotNull();
        verify(repository).create(any());
    }

    @Test
    void orderShouldNotBeCreatedIfNoAuthority() {
        var request = OrderCreateRequest.of(LIB_ITEM_ID, TYPE, TAKEN_PERIOD);

        when(security.getCurrentAuth()).thenReturn(Auths.guest());

        assertThatThrownBy(() -> orderService.make(request))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    void orderForReaderShouldBeCreated() {
        var request = OrderCreateRequest.of(LIB_ITEM_ID, TYPE, TAKEN_PERIOD);

        when(security.getCurrentAuth()).thenReturn(Auths.forLibrarian(LIBRARIAN_ID));

        Order order = orderService.makeForReader(request, READER_ID);
        assertThat(order)
                .returns(LIB_ITEM_ID, from(Order::getLibItemId))
                .returns(TYPE, from(Order::getType))
                .returns(TAKEN_PERIOD, from(Order::getTakenPeriod))
                .returns(READER_ID, from(Order::getReaderId))
                .returns(LIBRARIAN_ID, from(Order::getInitiatorId))
                .returns(OrderState.OPEN, from(Order::getState))
                .extracting(Order::getId).isNotNull();
        verify(repository).create(any());
    }

    @Test
    void orderForReaderShouldNotBeCreatedIfNoAuthority() {
        var request = OrderCreateRequest.of(LIB_ITEM_ID, TYPE, TAKEN_PERIOD);

        when(security.getCurrentAuth()).thenReturn(Auths.guest());

        assertThatThrownBy(() -> orderService.makeForReader(request, READER_ID))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    void orderShouldBeMarkedAccepted() {
        var orderId = randomUUID();
        var order = order(orderId, OrderState.OPEN);

        when(security.getCurrentAuth()).thenReturn(Auths.forLibrarian(LIBRARIAN_ID));
        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(repository.findByLibItemIdAndTakenPeriodIntersectsWith(LIB_ITEM_ID, TAKEN_PERIOD)).thenReturn(emptyList());

        orderService.markAccepted(orderId);

        verify(repository).update(argThat(updatedOrder -> updatedOrder.getState() == OrderState.ACCEPTED));
    }

    @Test
    void orderShouldBeAcceptedIfNoTakenPeriodConflict() {
        var orderId = randomUUID();
        var order = order(orderId, OrderState.OPEN);

        when(security.getCurrentAuth()).thenReturn(Auths.forLibrarian(LIBRARIAN_ID));
        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(repository.findByLibItemIdAndTakenPeriodIntersectsWith(LIB_ITEM_ID, TAKEN_PERIOD))
                .thenReturn(List.of(
                        order(OrderState.CANCELLED),
                        order(OrderState.RETURNED),
                        order(OrderState.OPEN)
                ));

        orderService.markAccepted(orderId);

        verify(repository).update(argThat(updatedOrder -> updatedOrder.getState() == OrderState.ACCEPTED));
    }

    @Test
    void orderShouldNotBeAcceptedWithTakenPeriodConflict() {
        var orderId = randomUUID();
        var order = order(orderId, OrderState.OPEN);

        when(security.getCurrentAuth()).thenReturn(Auths.forLibrarian(LIBRARIAN_ID));
        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(repository.findByLibItemIdAndTakenPeriodIntersectsWith(LIB_ITEM_ID, TAKEN_PERIOD))
                .thenReturn(singletonList(order(OrderState.ACCEPTED)));

        assertThatThrownBy(() -> orderService.markAccepted(orderId))
                .isInstanceOf(OrderException.class)
                .hasMessage(MessageKeys.Order.Error.TAKEN_PERIOD_CONFLICT);
    }

    @Test
    void orderShouldNotBeMarkedAcceptedFromInvalidState() {
        List.of(
                OrderState.ACCEPTED,
                OrderState.CANCELLED,
                OrderState.DELIVERED,
                OrderState.RETURNED

        ).forEach(invalidPreState -> {

            var orderId = randomUUID();
            var order = order(orderId, invalidPreState);

            when(security.getCurrentAuth()).thenReturn(Auths.forLibrarian(LIBRARIAN_ID));
            when(repository.findById(orderId)).thenReturn(Optional.of(order));
            when(repository.findByLibItemIdAndTakenPeriodIntersectsWith(LIB_ITEM_ID, TAKEN_PERIOD)).thenReturn(emptyList());

            assertThatThrownBy(() -> orderService.markAccepted(orderId))
                    .isInstanceOf(OrderException.class)
                    .hasMessage(MessageKeys.Order.Error.INVALID_STATE_CHANGE);
        });
    }
}
