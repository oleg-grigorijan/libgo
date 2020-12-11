package com.godev.libgo.domain.order;

import com.godev.libgo.domain.commons.exception.ApplicationException;
import com.godev.libgo.domain.commons.model.DateRange;

import java.util.UUID;

import static com.godev.libgo.MessageKeys.Order.Error.INVALID_STATE_CHANGE;
import static com.godev.libgo.MessageKeys.Order.Error.LIB_ITEM_IS_NOT_DELIVERED;
import static com.godev.libgo.MessageKeys.Order.Error.TAKEN_PERIOD_CONFLICT;

public class OrderException extends ApplicationException {

    private OrderException(String message) {
        super(message);
    }

    private OrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public static OrderException invalidStateChange() {
        return new OrderException(INVALID_STATE_CHANGE);
    }

    public static OrderException libItemIsNotDelivered(UUID libItemId) {
        return new OrderException(LIB_ITEM_IS_NOT_DELIVERED);
    }

    public static OrderException takenPeriodConflict(DateRange takenPeriod1, DateRange takenPeriod2) {
        return new OrderException(TAKEN_PERIOD_CONFLICT);
    }
}
