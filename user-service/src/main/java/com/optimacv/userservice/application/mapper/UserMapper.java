package com.optimacv.userservice.application.mapper;

import com.optimacv.userservice.application.dto.UserRequest;
import com.optimacv.userservice.application.dto.UserResponse;
import com.optimacv.userservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

}
