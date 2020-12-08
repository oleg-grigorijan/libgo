package com.godev.libgo.domain.user.service;

import com.godev.libgo.domain.user.model.Employee;
import com.godev.libgo.domain.user.model.EmployeeRegistrationRequest;

import java.util.UUID;

public interface EmployeeService {

    Employee getById(UUID id);

    Employee registerHr(EmployeeRegistrationRequest request);

    Employee registerLibrarian(EmployeeRegistrationRequest request);
}
