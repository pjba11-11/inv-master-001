package com.inv.invmaster001.mapper;

import com.inv.invmaster001.dto.entitydto.UserDTO;
import com.inv.invmaster001.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDto(User entity);
    User toEntity(UserDTO dto);
    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);
}