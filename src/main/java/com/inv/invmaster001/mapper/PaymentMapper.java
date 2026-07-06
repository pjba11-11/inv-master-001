package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.PaymentDTO;
import com.inv.invmaster001.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentDTO toDto(Payment entity);
    Payment toEntity(PaymentDTO dto);
    void updateEntityFromDto(PaymentDTO dto, @MappingTarget Payment entity);
}