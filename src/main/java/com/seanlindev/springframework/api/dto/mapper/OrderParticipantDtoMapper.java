package com.seanlindev.springframework.api.dto.mapper;

import com.seanlindev.springframework.api.dto.ParticipantOrderDto;
import com.seanlindev.springframework.api.request.ParticipantOrderRequestModel;
import org.modelmapper.ModelMapper;

public class OrderParticipantDtoMapper {
    public static ParticipantOrderDto convertToOrderParticipantDto(ParticipantOrderRequestModel participantOrderRequestModel) {
        ModelMapper modelMapper = new ModelMapper();
        ParticipantOrderDto participantOrderDto = modelMapper.map(participantOrderRequestModel, ParticipantOrderDto.class);
        return participantOrderDto;
    }
}
