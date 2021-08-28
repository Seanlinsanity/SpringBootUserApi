package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.OrderDto;
import com.seanlindev.springframework.api.dto.ParticipantOrderDto;
import com.seanlindev.springframework.exceptions.OrderServiceException;
import com.seanlindev.springframework.model.OrderStatus;
import com.seanlindev.springframework.model.entities.OrderEntity;
import com.seanlindev.springframework.model.entities.ParticipantOrderEntity;
import com.seanlindev.springframework.model.entities.UserEntity;
import com.seanlindev.springframework.repositories.OrderRepository;
import com.seanlindev.springframework.repositories.ParticipantOrderRepository;
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
    ParticipantOrderRepository participantOrderRepository;
    UserRepository userRepository;
    PublicIdGenerator publicIdGenerator;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ParticipantOrderRepository participantOrderRepository,
                            UserRepository userRepository,
                            PublicIdGenerator publicIdGenerator) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.publicIdGenerator = publicIdGenerator;
        this.participantOrderRepository = participantOrderRepository;
    }

    @Override
    public OrderDto createOrder(OrderDto order) {
        order.setOrderId(publicIdGenerator.generateOrderId(30));
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(order.getOrderId());
        orderEntity.setQuantity(order.getQuantity());
        orderEntity.setProductName(order.getProductName());
        orderEntity.setStatus(OrderStatus.CREATED);

        UserEntity ownerEntity = userRepository.findByUserId(order.getOwner().getUserId());
        if (ownerEntity == null) {
            throw new UsernameNotFoundException("Order owner not found: " + order.getOwner().getUserId());
        }
        orderEntity.setOwner(ownerEntity);

//        List<UserEntity> participants = order.getParticipantOrders().stream().map(orderParticipantDto -> {
//            UserEntity participantEntity = userRepository.findByUserId(orderParticipantDto.getUserId());
//            if (participantEntity == null) {
//                throw new UsernameNotFoundException("Order participant not found: " + orderParticipantDto.getUserId());
//            }
//            return participantEntity;
//        }).collect(Collectors.toList());
//        orderEntity.setParticipants(participants);

        List<ParticipantOrderEntity> participantOrders = order.getParticipantOrders().stream().map(participantOrderDto -> {
            ParticipantOrderEntity participantOrderEntity = new ParticipantOrderEntity();
            participantOrderEntity.setParentOrder(orderEntity);
            participantOrderEntity.setUserId(participantOrderDto.getUserId());
            participantOrderEntity.setQuantity(participantOrderDto.getQuantity());
            participantOrderEntity.setIdentity(publicIdGenerator.generateOrderId(30));
            return participantOrderEntity;
        }).collect(Collectors.toList());
        orderEntity.setParticipantOrders(participantOrders);

        OrderEntity savedOrder = orderRepository.save(orderEntity);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    public List<OrderDto> getOrdersByOwnerId(String ownerId) {
        UserEntity userEntity = userRepository.findByUserId(ownerId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Participant not found, id: " + ownerId);
        }
        List<OrderEntity> orderEntityList = orderRepository.findAllByOwnerId(userEntity.getId());
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<OrderDto>>() {}.getType();
        return modelMapper.map(orderEntityList, listType);
    }

    public OrderDto findByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        if (orderEntity == null) {
            throw new OrderServiceException("Order not found, order: " + orderId);
        }
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderEntity, OrderDto.class);
    }

    @Transactional
    @Override
    public OrderDto updateOrderParticipants(ParticipantOrderDto participantOrderDto) {
        OrderEntity orderEntity = orderRepository.findByOrderIdForShare(participantOrderDto.getOrderId());
        if (orderEntity == null) {
            throw new OrderServiceException("Order not found, order: " + participantOrderDto.getOrderId());
        }

        if (orderEntity.getStatus() == OrderStatus.PAID) {
            throw new OrderServiceException("You can not join an order which is already paid, order: " + participantOrderDto.getOrderId());
        }

        if (orderEntity.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderServiceException("You can not join an order which is already cancelled, order: " + participantOrderDto.getOrderId());
        }

        UserEntity userEntity = userRepository.findByUserId(participantOrderDto.getUserId());
        if (userEntity == null) {
            throw new UsernameNotFoundException("Participant not found, id: " + participantOrderDto.getUserId());
        }

        int result = orderRepository.addNewParticipantForOrder(orderEntity.getId(), userEntity.getId());

        ParticipantOrderEntity participantOrderEntity = new ParticipantOrderEntity();
        participantOrderEntity.setIdentity(publicIdGenerator.generateOrderId(30));
        participantOrderEntity.setQuantity(participantOrderDto.getQuantity());
        participantOrderEntity.setUserId(participantOrderDto.getUserId());
        participantOrderEntity.setParentOrder(orderEntity);
        participantOrderRepository.save(participantOrderEntity);

        ModelMapper modelMapper = new ModelMapper();
        OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);
        return orderDto;
    }

    @Transactional
    @Override
    public OrderDto updateOrderStatus(OrderDto orderDto) {
        OrderEntity orderEntity = orderRepository.findByOrderIdForUpdate(orderDto.getOrderId());
        if (orderEntity == null) {
            throw new OrderServiceException("Order not found: " + orderDto.getOrderId());
        }
        if (orderDto.getStatus() == OrderStatus.PAID && orderEntity.getStatus() == OrderStatus.PAID) {
            throw new OrderServiceException("Order is already paid, order: " + orderDto.getOrderId());
        }

        if (orderDto.getStatus() == OrderStatus.CANCELLED && orderEntity.getStatus() == OrderStatus.CANCELLED
            || orderDto.getStatus() == OrderStatus.CREATED) {
            throw new OrderServiceException("Invalid order paid status change, order: " + orderDto.getOrderId());
        }
        orderEntity.setStatus(orderDto.getStatus());
        orderRepository.save(orderEntity);

        ModelMapper modelMapper = new ModelMapper();
        OrderDto updatedOrderDto = modelMapper.map(orderEntity, OrderDto.class);
        return updatedOrderDto;
    }

    @Override
    public Boolean deleteOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        if (orderEntity == null) {
            return false;
        }
        orderRepository.delete(orderEntity);
        return true;
    }
}
