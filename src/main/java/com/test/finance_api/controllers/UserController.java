package com.test.finance_api.controllers;

import com.test.finance_api.dto.user.GetAllUserResponse;
import com.test.finance_api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<GetAllUserResponse> getAllUser() {
        return this.userService.getAllUsers();
    }
}
