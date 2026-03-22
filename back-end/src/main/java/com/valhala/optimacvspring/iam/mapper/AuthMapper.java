package com.valhala.optimacvspring.iam.mapper;

import com.valhala.optimacvspring.iam.dto.AuthResponseDTO;
import com.valhala.optimacvspring.iam.dto.RegisterRequestDTO;
import com.valhala.optimacvspring.iam.entities.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    AppUser toEntity(RegisterRequestDTO dto);

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.role", target = "role")
    @Mapping(source = "token", target = "token")
    AuthResponseDTO toAuthResponse(AppUser user, String token);
}
