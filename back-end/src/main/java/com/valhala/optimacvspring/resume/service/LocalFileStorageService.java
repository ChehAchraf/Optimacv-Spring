package com.valhala.optimacvspring.resume.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path baseUploadDir;

    public LocalFileStorageService(@Value("${app.storage.upload-dir}") String uploadDir) {
        this.baseUploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Override
    public String storeFile(MultipartFile file, UUID userId) {
        Path userDir = baseUploadDir.resolve(userId.toString());
        try {
            Files.createDirectories(userDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create user upload directory: " + userDir, e);
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String uniqueFilename = UUID.randomUUID() + extension;
        Path targetPath = userDir.resolve(uniqueFilename);

        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + uniqueFilename, e);
        }

        return "/uploads/resumes/" + userId + "/" + uniqueFilename;
    }
}
