package com.myspring.safechannel.cloudnary;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageUploadController {
	
    private final UploadService uploadService;
	
	@PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestPart(value = "file") MultipartFile file) {
        try {
            String imageUrl = uploadService.uploadFile(file);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }

}
