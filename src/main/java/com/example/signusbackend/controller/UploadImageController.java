package com.example.signusbackend.controller;

import java.net.URL;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.controller.dto.UploadRequest;
import com.example.signusbackend.service.GoogleStorageService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/uploads")
@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen por ahora, luego restringir según sea necesario

public class UploadImageController {
    
    private final GoogleStorageService googleStorageService;

    public UploadImageController(GoogleStorageService googleStorageService) {
        this.googleStorageService = googleStorageService;
    }

    @PostMapping("/signed-url")
    @Operation(summary = "Generar URL firmada para subir imagen", description = "Genera una URL firmada para subir una imagen a Google Cloud Storage.")
    public ResponseEntity<?> generarSignedUrl(@RequestBody UploadRequest req) {

        String filename = req.filename();
        String contentType = req.contentType();

        URL signedUrl = googleStorageService.generarSignedUrl(filename, contentType);

        String publicUrl = "https://storage.googleapis.com/" +
                googleStorageService.getBucketName() + "/" +
                googleStorageService.getFolder() + "/" + filename;
        return ResponseEntity.ok(Map.of(
            "uploadUrl", signedUrl.toString(),
            "publicUrl", publicUrl
        ));
    }
}
