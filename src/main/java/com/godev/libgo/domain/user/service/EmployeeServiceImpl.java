package com.godev.libgo.domain.user.service;

import com.godev.libgo.domain.commons.exception.EntityNotFoundException;
import com.godev.libgo.domain.commons.model.Email;
import com.godev.libgo.domain.commons.persistence.TxTemplate;
import com.godev.libgo.domain.commons.security.Auth;
import com.godev.libgo.domain.commons.security.Authority;
import com.godev.libgo.domain.commons.security.SecurityContext;
import com.godev.libgo.domain.user.UserException;
import com.godev.libgo.domain.user.model.Employee;
import com.godev.libgo.domain.user.model.EmployeeRegistrationRequest;
import com.godev.libgo.domain.user.model.UserRole;
import com.godev.libgo.domain.user.persistence.EmployeeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @NonNull private final EmployeeRepository employeeRepository;
    @NonNull private final SecurityContext security;
    @NonNull private final TxTemplate tx;

    @Override
    public Employee getById(@NonNull UUID id) {
        return tx.transactionalGet(() -> employeeRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(id)));
    }

    @Override
    public Employee registerHr(@NonNull EmployeeRegistrationRequest request) {
        return doRegister(request, UserRole.HR);
    }

    @Override
    public Employee registerLibrarian(@NonNull EmployeeRegistrationRequest request) {
        return doRegister(request, UserRole.LIBRARIAN);
    }

    private Employee doRegister(EmployeeRegistrationRequest request, UserRole role) {
        Auth auth = security.getCurrentAuth();
        auth.requireAuthority(Authority.CREATE_EMPLOYEE_USER);

        return tx.transactionalGet(() -> {
            Email email = request.getEmail();

            if (employeeRepository.existsByEmail(email)) {
                throw UserException.alreadyExistsByEmail(email);
            }

            Employee employee = new Employee(
                    UUID.randomUUID(),
                    request.getFullName(),
                    email,
                    role
            );
            employeeRepository.create(employee);
            return employee;
        });
    }
}
