package com.godev.libgo.domain.commons.security;

import com.godev.libgo.domain.user.model.User;
import com.godev.libgo.domain.user.model.UserRole;
import com.godev.libgo.infra.security.Auth;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.UUID;

import static com.godev.libgo.domain.commons.security.Authority.CANCEL_ORDER;
import static com.godev.libgo.domain.commons.security.Authority.CONFIRM_READER_IDENTITY;
import static com.godev.libgo.domain.commons.security.Authority.CREATE_EMPLOYEE_USER;
import static com.godev.libgo.domain.commons.security.Authority.EDIT_BOOK_REGISTRY;
import static com.godev.libgo.domain.commons.security.Authority.EDIT_LIB_CATALOG;
import static com.godev.libgo.domain.commons.security.Authority.MAKE_ORDER_FOR_OTHER;
import static com.godev.libgo.domain.commons.security.Authority.MAKE_ORDER_FOR_SELF;
import static com.godev.libgo.domain.commons.security.Authority.PROCESS_ORDER;
import static com.godev.libgo.domain.commons.security.Authority.VIEW_ANY_ORDER;
import static com.godev.libgo.domain.commons.security.Authority.VIEW_ANY_READER;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@UtilityClass
public class Auths {

    private static final Auth GUEST_AUTH = new Auth(null, UserRole.GUEST, emptyList());
    private static final Auth SUPER_AUTH = new Auth(null, UserRole.SUPER, asList(Authority.values()));

    public static Auth forUser(User user) {
        var id = user.getId();
        return switch (user.getRole()) {
            case GUEST -> guest();
            case LIBRARIAN -> forLibrarian(id);
            case HR -> forHr(id);
            case READER -> forReader(id);
            case SUPER -> superUser();
        };
    }

    public static Auth guest() {
        return GUEST_AUTH;
    }

    public static Auth superUser() {
        return SUPER_AUTH;
    }

    public static Auth forReader(UUID readerId) {
        return new Auth(readerId, UserRole.READER, List.of(
                MAKE_ORDER_FOR_SELF,
                CANCEL_ORDER
        ));
    }

    public static Auth forLibrarian(UUID librarianId) {
        return new Auth(librarianId, UserRole.LIBRARIAN, List.of(
                VIEW_ANY_READER,
                CONFIRM_READER_IDENTITY,
                EDIT_BOOK_REGISTRY,
                EDIT_LIB_CATALOG,
                VIEW_ANY_ORDER,
                MAKE_ORDER_FOR_OTHER,
                PROCESS_ORDER,
                CANCEL_ORDER
        ));
    }

    public static Auth forHr(UUID hrId) {
        return new Auth(hrId, UserRole.HR, List.of(
                CREATE_EMPLOYEE_USER
        ));
    }
}
