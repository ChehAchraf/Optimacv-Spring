package com.optimacv.userservice.application.service.interfaces;

import com.optimacv.userservice.application.dto.UserRequest;
import com.optimacv.userservice.application.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    List<UserResponse> getAllUsers();
}
