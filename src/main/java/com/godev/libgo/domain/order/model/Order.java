package com.godev.libgo.domain.order.model;

import com.godev.libgo.domain.commons.model.DateRange;
import com.godev.libgo.domain.commons.model.DomainEntity;
import com.godev.libgo.domain.order.OrderException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Order implements DomainEntity {

    private final UUID id;
    private final UUID libItemId;
    private final UUID readerId;
    private final UUID initiatorId;
    private final DateRange takenPeriod;
    private final OrderType type;
    private OrderState state;

    public static Order forCreation(UUID libItemId, UUID readerId, UUID initiatorId, DateRange takenPeriod, OrderType type) {
        return new Order(
                UUID.randomUUID(),
                libItemId,
                readerId,
                initiatorId,
                takenPeriod,
                type,
                OrderState.OPEN
        );
    }

    public void markAccepted() {
        if (state == OrderState.OPEN) {
            state = OrderState.ACCEPTED;
        } else {
            throw OrderException.invalidStateChange();
        }
    }

    public void markDelivered() {
        if (state == OrderState.ACCEPTED) {
            state = OrderState.DELIVERED;
        } else {
            throw OrderException.invalidStateChange();
        }
    }

    public void markReturned() {
        if (state == OrderState.DELIVERED) {
            state = OrderState.RETURNED;
        } else {
            throw OrderException.invalidStateChange();
        }
    }

    public void markCancelled() {
        if (state == OrderState.OPEN || state == OrderState.ACCEPTED) {
            state = OrderState.CANCELLED;
        } else {
            throw OrderException.invalidStateChange();
        }
    }
}
