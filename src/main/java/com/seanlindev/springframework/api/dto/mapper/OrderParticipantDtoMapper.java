package com.seanlindev.springframework.api.dto.mapper;

import com.seanlindev.springframework.api.dto.OrderParticipantDto;
import com.seanlindev.springframework.api.request.OrderParticipantsRequestModel;
import org.modelmapper.ModelMapper;

public class OrderParticipantDtoMapper {
    public static OrderParticipantDto convertToOrderParticipantDto(OrderParticipantsRequestModel orderParticipantsRequestModel) {
        ModelMapper modelMapper = new ModelMapper();
        OrderParticipantDto orderParticipantDto = modelMapper.map(orderParticipantsRequestModel, OrderParticipantDto.class);
        return orderParticipantDto;
    }
}
