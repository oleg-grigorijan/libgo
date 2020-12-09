package com.godev.libgo.domain.user.persistence;

import com.godev.libgo.domain.commons.model.Email;
import com.godev.libgo.domain.commons.persistence.Repository;
import com.godev.libgo.domain.user.model.Employee;
import lombok.NonNull;

public interface EmployeeRepository extends Repository<Employee> {

    boolean existsByEmail(@NonNull Email email);
}
