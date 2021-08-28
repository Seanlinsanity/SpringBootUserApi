package com.seanlindev.springframework.api.dto.mapper;

import com.seanlindev.springframework.api.dto.ParticipantOrderDto;
import com.seanlindev.springframework.api.request.OrderParticipantsRequestModel;
import org.modelmapper.ModelMapper;

public class OrderParticipantDtoMapper {
    public static ParticipantOrderDto convertToOrderParticipantDto(OrderParticipantsRequestModel orderParticipantsRequestModel) {
        ModelMapper modelMapper = new ModelMapper();
        ParticipantOrderDto participantOrderDto = modelMapper.map(orderParticipantsRequestModel, ParticipantOrderDto.class);
        return participantOrderDto;
    }
}
