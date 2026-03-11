package com.valhala.optimacvspring.iam.service;

import com.valhala.optimacvspring.iam.api.IamApi;
import com.valhala.optimacvspring.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IamApiImpl implements IamApi {

    private final UserRepository userRepository;

    @Override
    public UUID findUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email))
                .getId();
    }
}
