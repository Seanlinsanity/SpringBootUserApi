package com.seanlindev.springframework.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @GetMapping
    public String getUser() {
        return "get user here...";
    }

    @PostMapping
    public String createUser() {
        return "create user here...";
    }

    @PutMapping
    public String updateUser() {
        return "update user here...";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user here...";
    }
}
