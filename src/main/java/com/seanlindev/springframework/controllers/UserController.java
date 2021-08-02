package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.api.request.RequestOperationName;
import com.seanlindev.springframework.api.request.UserDetailsRequestModel;
import com.seanlindev.springframework.api.response.ErrorMessageType;
import com.seanlindev.springframework.api.response.OperationStatusModel;
import com.seanlindev.springframework.api.response.RequestOperationStatus;
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

    @GetMapping(path="/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserRest getUser(@PathVariable String id) {
        UserRest userRest = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, userRest);
        return userRest;
    }

    @PostMapping(
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessageType.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserRest userRest = new UserRest();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, userRest);

        return userRest;
    }

    @PutMapping(path="/{id}",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessageType.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserRest userRest = new UserRest();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createdUser = userService.updateUserByUserId(id, userDto);
        BeanUtils.copyProperties(createdUser, userRest);

        return userRest;
    }

    @DeleteMapping(path="/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel statusModel = new OperationStatusModel();
        statusModel.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUserByUserId(id);
        statusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return statusModel;
    }
}
