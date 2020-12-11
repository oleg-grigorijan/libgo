package com.godev.libgo.domain.commons.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import static com.godev.libgo.MessageKeys.Commons.Error.START_DATE_IS_AFTER_END_DATE;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class DateRange implements DomainValue {

    private final LocalDate from;
    private final LocalDate to;

    public static DateRange of(@NonNull LocalDate from, @NonNull LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException(START_DATE_IS_AFTER_END_DATE);
        }
        return new DateRange(from, to);
    }

    public static DateRange ofSingleDay(@NonNull LocalDate date) {
        return new DateRange(date, date);
    }
}
