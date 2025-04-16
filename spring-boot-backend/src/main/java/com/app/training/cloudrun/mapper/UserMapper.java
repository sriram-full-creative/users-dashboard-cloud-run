package com.app.training.cloudrun.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.app.training.cloudrun.dto.UserDto;
import com.app.training.cloudrun.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);
}
