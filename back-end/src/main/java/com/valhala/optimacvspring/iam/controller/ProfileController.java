package com.valhala.optimacvspring.iam.controller;

import com.valhala.optimacvspring.iam.dto.ChangePasswordRequestDTO;
import com.valhala.optimacvspring.iam.dto.UpdateProfileRequestDTO;
import com.valhala.optimacvspring.iam.dto.UserProfileResponseDTO;
import com.valhala.optimacvspring.iam.entities.AppUser;
import com.valhala.optimacvspring.iam.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getCurrentProfile(
            @AuthenticationPrincipal AppUser currentUser
    ) {
        return ResponseEntity.ok(profileService.getCurrentUserProfile(currentUser));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> updateProfile(
            @AuthenticationPrincipal AppUser currentUser,
            @Valid @RequestBody UpdateProfileRequestDTO request
    ) {
        return ResponseEntity.ok(profileService.updateProfile(currentUser, request));
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal AppUser currentUser,
            @Valid @RequestBody ChangePasswordRequestDTO request
    ) {
        profileService.changePassword(currentUser, request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
