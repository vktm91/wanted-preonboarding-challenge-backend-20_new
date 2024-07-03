package com.example.demo.user.service.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);

    User getById(long id);

    User save(User user);
}
