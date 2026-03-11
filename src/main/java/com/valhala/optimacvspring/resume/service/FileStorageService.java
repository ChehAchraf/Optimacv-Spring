package com.valhala.optimacvspring.resume.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileStorageService {

    String storeFile(MultipartFile file, UUID userId);
}
