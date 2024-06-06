package com.example.demo.user.controller;

import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.controller.response.UserResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Builder
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(UserResponse.from(userService.getById(id)));
    }
}