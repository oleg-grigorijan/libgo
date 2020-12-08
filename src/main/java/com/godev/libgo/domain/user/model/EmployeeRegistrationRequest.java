package com.godev.libgo.domain.user.model;

import com.godev.libgo.domain.commons.model.Email;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EmployeeRegistrationRequest implements Serializable {

    private String fullName;
    private Email email;
}
