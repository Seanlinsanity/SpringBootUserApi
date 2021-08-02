package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.api.request.UserDetailsRequestModel;
import com.seanlindev.springframework.api.response.ErrorMessageType;
import com.seanlindev.springframework.api.response.UserRest;
import com.seanlindev.springframework.exceptions.UserServiceException;
import com.seanlindev.springframework.services.UserService;
import com.seanlindev.springframework.api.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path="/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest getUser(@PathVariable String id) {
        UserRest userRest = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, userRest);
        return userRest;
    }

    @PostMapping(
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessageType.MISSING_REQUIRED_FIELD.getErrorMessage());

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
