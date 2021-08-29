package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.api.dto.OrderDto;
import com.seanlindev.springframework.api.request.RequestOperationName;
import com.seanlindev.springframework.api.request.UserDetailsRequestModel;
import com.seanlindev.springframework.api.response.*;
import com.seanlindev.springframework.exceptions.UserServiceException;
import com.seanlindev.springframework.services.OrderService;
import com.seanlindev.springframework.services.UserService;
import com.seanlindev.springframework.api.dto.UserDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @ApiOperation(value = "Get a user details service end point",
                  notes = "Specify user public id in URL path to get the user info. For example: /users/abcdefg123456")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserResponse getUser(@PathVariable String id) {
        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        UserResponse userResponse = modelMapper.map(userDto, UserResponse.class);
        return userResponse;
    }

    @ApiOperation(value = "Create a new user service end point",
                  notes = "Provide user credentials info and profile details in request body to create a new user")
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserResponse createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessageType.MISSING_REQUIRED_FIELD.getErrorMessage());

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        UserResponse userResponse = modelMapper.map(createdUser, UserResponse.class);
        return userResponse;
    }

    @ApiOperation(value = "Update an existed user details service end point",
                  notes = "Provide user details in request body to update an existed user info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessageType.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserResponse userResponse = new UserResponse();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createdUser = userService.updateUserByUserId(id, userDto);
        BeanUtils.copyProperties(createdUser, userResponse);

        return userResponse;
    }

    @ApiOperation(value = "Delete an existed user service end point",
                  notes = "Specify user public id in URL path to delete an user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @DeleteMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel statusModel = new OperationStatusModel();
        statusModel.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUserByUserId(id);
        statusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return statusModel;
    }

    @ApiOperation(value = "Get the user list service end point",
                  notes = "This API supports page and limit as parameter to get user list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserResponse> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "limit", defaultValue = "25") int limit) {
        return userService.getUsers(page, limit).stream()
                .map(userDto -> {
                    ModelMapper modelMapper = new ModelMapper();
                    UserResponse userResponse = modelMapper.map(userDto, UserResponse.class);
                    return userResponse;
                }).collect(Collectors.toList());
    }

    @ApiOperation(value = "Get the user address details service end point",
                  notes = "Specify user public id in URL path to get an user address info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<AddressResponse> getUserAddresses(@PathVariable String id) {
        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<AddressResponse>>() {
        }.getType();
        return modelMapper.map(userDto.getAddresses(), listType);
    }

    @ApiOperation(value = "Get orders which is owned by an user service end point",
                  notes = "Specify user public id in URL path to get orders which a user owns")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @GetMapping("/{id}/owned-orders")
    public List<OrderResponse> getUserOwnedOrders(@PathVariable String id) {
        List<OrderDto> orderDtoList = orderService.getOrdersByOwnerId(id);
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<OrderResponse>>() {
        }.getType();
        return modelMapper.map(orderDtoList, listType);
    }
}
