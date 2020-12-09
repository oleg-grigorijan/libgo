package com.godev.libgo.domain.commons.security;

public enum Authority {

    VIEW_ANY_READER,
    CREATE_EMPLOYEE_USER,
    CONFIRM_READER_IDENTITY,

    EDIT_BOOK_REGISTRY,
    EDIT_LIB_CATALOG,

    VIEW_ANY_ORDER,
    MAKE_ORDER_FOR_SELF,
    MAKE_ORDER_FOR_OTHER,
    PROCESS_ORDER,
    CANCEL_ORDER,
}
