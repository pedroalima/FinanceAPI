package com.test.finance_api.controller;

import com.test.finance_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService _userService;

//    @GetMapping("/get")
//    public User getUser() {
//        return _userService.getUser();
//    }

//    @PostMapping("/{id}")
//    public String postUser(@PathVariable("id") String id, @RequestBody User user) {
//        return "Helo " + user.getName() + " " + id;
//    }
}
