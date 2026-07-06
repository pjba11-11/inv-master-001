package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.CompanyDTO;
import com.inv.invmaster001.entity.Company;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T16:43:28+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Amazon.com Inc.)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyDTO toDto(Company entity) {
        if ( entity == null ) {
            return null;
        }

        CompanyDTO companyDTO = new CompanyDTO();

        return companyDTO;
    }

    @Override
    public Company toEntity(CompanyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Company company = new Company();

        return company;
    }

    @Override
    public void updateEntityFromDto(CompanyDTO dto, Company entity) {
        if ( dto == null ) {
            return;
        }
    }
}
