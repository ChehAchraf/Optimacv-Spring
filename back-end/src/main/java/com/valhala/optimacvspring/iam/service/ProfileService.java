package com.valhala.optimacvspring.iam.service;

import com.valhala.optimacvspring.iam.dto.ChangePasswordRequestDTO;
import com.valhala.optimacvspring.iam.dto.UpdateProfileRequestDTO;
import com.valhala.optimacvspring.iam.dto.UserProfileResponseDTO;
import com.valhala.optimacvspring.iam.entities.AppUser;

public interface ProfileService {

    UserProfileResponseDTO getCurrentUserProfile(AppUser currentUser);

    UserProfileResponseDTO updateProfile(AppUser currentUser, UpdateProfileRequestDTO dto);

    void changePassword(AppUser currentUser, ChangePasswordRequestDTO dto);
}
