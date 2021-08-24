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

    @ApiOperation(value = "Get user details service end point",
                  notes = "Specify user public id in URL path. For example: /users/abcdefg123456")
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserResponse getUser(@PathVariable String id) {
        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        UserResponse userResponse = modelMapper.map(userDto, UserResponse.class);
        return userResponse;
    }

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

    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<AddressResponse> getUserAddresses(@PathVariable String id) {
        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<AddressResponse>>() {
        }.getType();
        return modelMapper.map(userDto.getAddresses(), listType);
    }

    @GetMapping("/{id}/owned-orders")
    public List<OrderResponse> getUserOwnedOrders(@PathVariable String id) {
        List<OrderDto> orderDtoList = orderService.getOrdersByOwnerId(id);
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<OrderResponse>>() {
        }.getType();
        return modelMapper.map(orderDtoList, listType);
    }
}
