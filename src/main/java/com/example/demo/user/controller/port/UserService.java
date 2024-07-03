package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;

public interface UserService {
    User getById(long id);
    User create(UserCreate userCreate);
//    void login(long id);
}
