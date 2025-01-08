package com.ecom.shoping_cart.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUploadUtil {

    public void saveFile(String fileDir, String fileName , MultipartFile file) {
        try {

            Path rootPath = Paths.get("src/main/resources/static/image", fileDir);
            File saveDir = rootPath.toFile();

            // Create directories if not existing
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            // Resolve the full path for the file
            Path path = rootPath.resolve(fileName);
            System.out.println("Path: " + path);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + fileName, e);
        }
    }
}
