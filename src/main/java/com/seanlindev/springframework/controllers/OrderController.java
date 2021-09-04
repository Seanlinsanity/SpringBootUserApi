package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.api.dto.OrderDto;
import com.seanlindev.springframework.api.dto.ParticipantOrderDto;
import com.seanlindev.springframework.api.dto.ShipmentDto;
import com.seanlindev.springframework.api.dto.mapper.OrderDtoMapper;
import com.seanlindev.springframework.api.dto.mapper.OrderParticipantDtoMapper;
import com.seanlindev.springframework.api.request.OrderDetailsRequestModel;
import com.seanlindev.springframework.api.request.OrderStatusRequestModel;
import com.seanlindev.springframework.api.request.ParticipantOrderRequestModel;
import com.seanlindev.springframework.api.request.RequestOperationName;
import com.seanlindev.springframework.api.response.OperationStatusModel;
import com.seanlindev.springframework.api.response.OrderResponse;
import com.seanlindev.springframework.api.response.RequestOperationStatus;
import com.seanlindev.springframework.model.OrderStatus;
import com.seanlindev.springframework.services.OrderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("orders")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "Create a new order service end point",
                  notes = "Provide order details in request body to create a new order")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "header")
    })
    @PostMapping
    public OrderResponse createNewOrder(@RequestBody OrderDetailsRequestModel orderDetails) throws Exception {
        OrderDto orderDto = OrderDtoMapper.convertToOrderDto(orderDetails);
        OrderDto createdOrderDto = orderService.createOrder(orderDto);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(createdOrderDto, OrderResponse.class);
    }

    @ApiOperation(value = "Get an order details service end point",
                  notes = "Specify order public id in URL path to get the order info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "header")
    })
    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable String id) {
        OrderDto orderDto = orderService.findByOrderId(id);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }

    @ApiOperation(value = "Join an order to become a participant service end point",
                  notes = "Specify order public id in URL path and provide participant details in request body to add new participant")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "header")
    })
    @PutMapping("/{id}/participants")
    public OrderResponse updateOrderParticipants(@PathVariable String id,
                                                 @RequestBody ParticipantOrderRequestModel participantOrderRequestModel) throws Exception {
        ParticipantOrderDto participantOrderDto = OrderParticipantDtoMapper.convertToOrderParticipantDto(participantOrderRequestModel);
        participantOrderDto.setOrderId(id);
        OrderDto orderDto = orderService.updateOrderParticipants(participantOrderDto);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }

    @ApiOperation(value = "Update an order status service end point",
                  notes = "Specify order public id in URL path and provide new status in request body to update order status. " +
                          "The status value can be 'CREATED', 'PAID', 'CANCELLED'")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "header")
    })
    @PutMapping("/{id}/status")
    public OrderResponse updateOrderStatus(@PathVariable String id,
                                               @RequestBody OrderStatusRequestModel orderStatusRequestModel) throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(id);
        try {
            OrderStatus status = OrderStatus.valueOf(orderStatusRequestModel.getStatus());
            orderDto.setStatus(status);
            OrderDto resultOrderDto = orderService.changeOrderStatus(orderDto);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(resultOrderDto, OrderResponse.class);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @ApiOperation(value = "Delete an order service end point",
                  notes = "Specify order public id in URL path to delete an order")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "header")
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

    @ApiOperation(value = "Get an order shipment details end point",
                  notes = "Specify order public id in URL path to get shipment details")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "header")
    })
    @GetMapping("/{id}/shipment")
    public ShipmentDto getOrderShipmentDetails(@PathVariable String id) throws Exception {
        return orderService.getOrderShipmentDetails(id);
    }

}
