package com.godev.libgo.domain.order.model;

import com.godev.libgo.domain.commons.model.DomainEntity;
import com.godev.libgo.domain.order.OrderException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Order implements DomainEntity {

    private final UUID id;
    private final UUID libItemId;
    private final UUID readerId;
    private final UUID initiatorId;
    private final LocalDate takenFromDate;
    private final LocalDate takenToDate;
    private final OrderType type;
    private OrderState state;

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
