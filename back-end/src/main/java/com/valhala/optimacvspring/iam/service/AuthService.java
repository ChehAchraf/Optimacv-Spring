package com.valhala.optimacvspring.iam.service;

import com.valhala.optimacvspring.iam.dto.AuthResponseDTO;
import com.valhala.optimacvspring.iam.dto.LoginRequestDTO;
import com.valhala.optimacvspring.iam.dto.RegisterRequestDTO;

public interface AuthService {

    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);
}
