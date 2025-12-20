package com.optimacv.userservice.application.service;

import com.optimacv.userservice.application.dto.UserRequest;
import com.optimacv.userservice.application.dto.UserResponse;
import com.optimacv.userservice.application.mapper.UserMapper;
import com.optimacv.userservice.application.service.interfaces.UserService;
import com.optimacv.userservice.domain.model.User;
import com.optimacv.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new RuntimeException("email Already exists");
        }
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().
                stream()
                .map(userMapper::toResponse)
                .toList();
    }
}
