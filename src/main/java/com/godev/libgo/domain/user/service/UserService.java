package com.godev.libgo.domain.user.service;

import com.godev.libgo.domain.user.model.User;

import java.util.UUID;

public interface UserService {

    User getById(UUID id);
}
