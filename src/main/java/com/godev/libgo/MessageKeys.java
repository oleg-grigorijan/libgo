package com.godev.libgo;

public interface MessageKeys {

    interface Commons {
        interface Error {
            String BADLY_FORMATTED_EMAIL = "email.badlyFormatted";
            String UNKNOWN_LANGUAGE = "language.unknown";
            String START_DATE_IS_AFTER_END_DATE = "dateRange.invalid";
        }
    }

    interface Security {
        String BASE = "security";

        interface Error {
            String BASE = Security.BASE + ".error";
            String NO_AUTHORITY = BASE + ".authority.absent";
        }
    }

    interface User {
        String BASE = "user";

        interface Error {
            String BASE = User.BASE + ".error";
            String ALREADY_EXISTS_BY_EMAIL = BASE + ".registration.email.alreadyExists";
        }
    }

    interface Order {
        String BASE = "order";

        interface Error {
            String BASE = Order.BASE + ".error";
            String INVALID_STATE_CHANGE = BASE + ".state.invalidChange";
            String LIB_ITEM_IS_NOT_DELIVERED = BASE + ".libItem.notDelivered";
            String TAKEN_PERIOD_CONFLICT = BASE + ".takenPeriod.conflict";
        }
    }
}
