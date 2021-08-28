package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.api.dto.OrderDto;
import com.seanlindev.springframework.api.dto.ParticipantOrderDto;
import com.seanlindev.springframework.api.dto.mapper.OrderDtoMapper;
import com.seanlindev.springframework.api.dto.mapper.OrderParticipantDtoMapper;
import com.seanlindev.springframework.api.request.OrderDetailsRequestModel;
import com.seanlindev.springframework.api.request.OrderStatusRequestModel;
import com.seanlindev.springframework.api.request.OrderParticipantsRequestModel;
import com.seanlindev.springframework.api.request.RequestOperationName;
import com.seanlindev.springframework.api.response.ErrorMessage;
import com.seanlindev.springframework.api.response.OperationStatusModel;
import com.seanlindev.springframework.api.response.OrderResponse;
import com.seanlindev.springframework.api.response.RequestOperationStatus;
import com.seanlindev.springframework.model.OrderStatus;
import com.seanlindev.springframework.services.OrderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("orders")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @PostMapping
    public OrderResponse createNewOrder(@RequestBody OrderDetailsRequestModel orderDetails) throws Exception {
        OrderDto orderDto = orderService.createOrder(OrderDtoMapper.convertToOrderDto(orderDetails));
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable String id) {
        OrderDto orderDto = orderService.findByOrderId(id);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @PutMapping("/{id}/participants")
    public OrderResponse updateOrderParticipants(@PathVariable String id,
                                                 @RequestBody OrderParticipantsRequestModel orderParticipantsRequestModel) throws Exception {
        ParticipantOrderDto participantOrderDto = OrderParticipantDtoMapper.convertToOrderParticipantDto(orderParticipantsRequestModel);
        participantOrderDto.setOrderId(id);
        OrderDto orderDto = orderService.updateOrderParticipants(participantOrderDto);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @PutMapping("/{id}/status")
    public OrderResponse updateOrderStatus(@PathVariable String id,
                                               @RequestBody OrderStatusRequestModel orderStatusRequestModel) throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(id);
        try {
            OrderStatus status = OrderStatus.valueOf(orderStatusRequestModel.getStatus());
            orderDto.setStatus(status);
            OrderDto resultOrderDto = orderService.updateOrderStatus(orderDto);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(resultOrderDto, OrderResponse.class);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<OperationStatusModel> deleteOrder(@PathVariable String id) throws Exception {
        OperationStatusModel statusModel = new OperationStatusModel();
        statusModel.setOperationName(RequestOperationName.DELETE.name());
        Boolean success = orderService.deleteOrderByOrderId(id);
        if (success) {
            statusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
            return new ResponseEntity<>(statusModel, HttpStatus.OK);
        } else {
            statusModel.setOperationResult(RequestOperationStatus.FAIL.name());
            return new ResponseEntity<>(statusModel, HttpStatus.BAD_REQUEST);
        }
    }

}
