package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.CustomerDTO;
import com.inv.invmaster001.entity.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDTO toDto(Customer entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        return customerDTO;
    }

    @Override
    public Customer toEntity(CustomerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Customer customer = new Customer();

        return customer;
    }

    @Override
    public void updateEntityFromDto(CustomerDTO dto, Customer entity) {
        if ( dto == null ) {
            return;
        }
    }
}
