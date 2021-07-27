package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.model.request.UserDetailsRequestModel;
import com.seanlindev.springframework.model.response.UserRest;
import com.seanlindev.springframework.services.UserService;
import com.seanlindev.springframework.model.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUser() {
        return "get user here...";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRest userRest = new UserRest();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, userRest);

        return userRest;
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
