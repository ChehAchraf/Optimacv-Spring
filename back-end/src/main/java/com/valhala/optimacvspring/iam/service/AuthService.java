package com.valhala.optimacvspring.iam.service;

import com.valhala.optimacvspring.iam.dto.AuthResponseDTO;
import com.valhala.optimacvspring.iam.dto.LoginRequestDTO;
import com.valhala.optimacvspring.iam.dto.RegisterRequestDTO;
import com.valhala.optimacvspring.iam.entities.AppUser;
import com.valhala.optimacvspring.iam.enums.Role;
import com.valhala.optimacvspring.iam.exception.EmailAlreadyExistsException;
import com.valhala.optimacvspring.iam.mapper.AuthMapper;
import com.valhala.optimacvspring.iam.repository.UserRepository;
import com.valhala.optimacvspring.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsAppUserByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }


        AppUser user = authMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        if(request.role() != null && request.role().equalsIgnoreCase("ROLE_COMPANY")){
            user.setRole(Role.ROLE_COMPANY);
        }else {
            user.setRole(Role.ROLE_USER);
        }

        AppUser savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);
        return authMapper.toAuthResponse(savedUser, token);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication"));

        String token = jwtService.generateToken(user);
        return authMapper.toAuthResponse(user, token);
    }
}
