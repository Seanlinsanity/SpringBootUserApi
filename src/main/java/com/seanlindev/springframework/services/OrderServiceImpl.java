package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.OrderDto;
import com.seanlindev.springframework.api.dto.OrderParticipantDto;
import com.seanlindev.springframework.model.entities.OrderEntity;
import com.seanlindev.springframework.model.entities.UserEntity;
import com.seanlindev.springframework.repositories.OrderRepository;
import com.seanlindev.springframework.repositories.UserRepository;
import com.seanlindev.springframework.shared.utils.PublicIdGenerator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;
    PublicIdGenerator publicIdGenerator;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            PublicIdGenerator publicIdGenerator) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.publicIdGenerator = publicIdGenerator;
    }

    @Override
    public OrderDto createOrder(OrderDto order) {
        order.setOrderId(publicIdGenerator.generateOrderId(30));
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(order.getOrderId());
        orderEntity.setQuantity(order.getQuantity());
        orderEntity.setProductName(order.getProductName());

        UserEntity ownerEntity = userRepository.findByUserId(order.getOwner().getUserId());
        if (ownerEntity == null) {
            throw new UsernameNotFoundException("Order owner not found: " + order.getOwner().getUserId());
        }
        orderEntity.setOwner(ownerEntity);

        List<UserEntity> participants = order.getParticipants().stream().map(userDto -> {
            UserEntity participantEntity = userRepository.findByUserId(userDto.getUserId());
            if (participantEntity == null) {
                throw new UsernameNotFoundException("Order participant not found: " + userDto.getUserId());
            }
            return participantEntity;
        }).collect(Collectors.toList());
        orderEntity.setParticipants(participants);

        OrderEntity savedOrder = orderRepository.save(orderEntity);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    public List<OrderDto> getOrdersByOwnerId(String ownerId) {
        UserEntity userEntity = userRepository.findByUserId(ownerId);
        List<OrderEntity> orderEntityList = orderRepository.findAllByOwnerId(userEntity.getId());
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<OrderDto>>() {}.getType();
        return modelMapper.map(orderEntityList, listType);
    }

    public OrderDto findByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderEntity, OrderDto.class);
    }

    @Transactional
    @Override
    public OrderDto updateOrderParticipants(OrderParticipantDto orderParticipantDto) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderParticipantDto.getOrderId());
        if (orderEntity == null) {
            throw new RuntimeException("Order not found: " + orderParticipantDto.getOrderId());
        }
        UserEntity userEntity = userRepository.findByUserId(orderParticipantDto.getParticipantId());
        if (userEntity == null) {
            throw new RuntimeException("Participant not found " + orderParticipantDto.getParticipantId());
        }

        Integer newQuantity = orderEntity.getQuantity() + orderParticipantDto.getQuantity();
        int result = orderRepository.addNewParticipantForOrder(orderEntity.getId(), userEntity.getId());
        System.out.println("add new participant result: " + result);
        orderRepository.updateOrderQuantity(orderEntity.getId(), newQuantity);

        ModelMapper modelMapper = new ModelMapper();
        OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);
        orderDto.setQuantity(newQuantity);
        return orderDto;
    }
}
