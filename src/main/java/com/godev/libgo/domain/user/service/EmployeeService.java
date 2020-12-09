package com.godev.libgo.domain.user.service;

import com.godev.libgo.domain.user.model.Employee;
import com.godev.libgo.domain.user.model.EmployeeRegistrationRequest;
import lombok.NonNull;

import java.util.UUID;

public interface EmployeeService {

    Employee getById(@NonNull UUID id);

    Employee registerHr(@NonNull EmployeeRegistrationRequest request);

    Employee registerLibrarian(@NonNull EmployeeRegistrationRequest request);
}
