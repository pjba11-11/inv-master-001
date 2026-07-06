package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.PaymentDTO;
import com.inv.invmaster001.entity.Payment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentDTO toDto(Payment entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentDTO paymentDTO = new PaymentDTO();

        return paymentDTO;
    }

    @Override
    public Payment toEntity(PaymentDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Payment payment = new Payment();

        return payment;
    }

    @Override
    public void updateEntityFromDto(PaymentDTO dto, Payment entity) {
        if ( dto == null ) {
            return;
        }
    }
}
