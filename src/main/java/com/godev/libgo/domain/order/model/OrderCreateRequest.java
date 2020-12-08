package com.godev.libgo.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderCreateRequest implements Serializable {

    private UUID libItemId;
    private OrderType type;
    private LocalDate takenFromDate;
    private LocalDate takenToDate;
}
