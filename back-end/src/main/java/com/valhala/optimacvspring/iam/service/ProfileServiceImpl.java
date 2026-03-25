package com.valhala.optimacvspring.iam.service;

import com.valhala.optimacvspring.iam.dto.ChangePasswordRequestDTO;
import com.valhala.optimacvspring.iam.dto.UpdateProfileRequestDTO;
import com.valhala.optimacvspring.iam.dto.UserProfileResponseDTO;
import com.valhala.optimacvspring.iam.entities.AppUser;
import com.valhala.optimacvspring.iam.exception.EmailAlreadyExistsException;
import com.valhala.optimacvspring.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileResponseDTO getCurrentUserProfile(AppUser currentUser) {
        return new UserProfileResponseDTO(
                currentUser.getId(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getEmail(),
                currentUser.getRole().name()
        );
    }

    @Override
    @Transactional
    public UserProfileResponseDTO updateProfile(AppUser currentUser, UpdateProfileRequestDTO dto) {
        if (!currentUser.getEmail().equalsIgnoreCase(dto.email()) &&
                userRepository.existsByEmailAndIdNot(dto.email(), currentUser.getId())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        currentUser.setFirstName(dto.firstName());
        currentUser.setLastName(dto.lastName());
        currentUser.setEmail(dto.email());

        AppUser updatedUser = userRepository.save(currentUser);

        return new UserProfileResponseDTO(
                updatedUser.getId(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getEmail(),
                updatedUser.getRole().name()
        );
    }

    @Override
    @Transactional
    public void changePassword(AppUser currentUser, ChangePasswordRequestDTO dto) {
        if (!passwordEncoder.matches(dto.currentPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password");
        }

        currentUser.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(currentUser);
    }
}
