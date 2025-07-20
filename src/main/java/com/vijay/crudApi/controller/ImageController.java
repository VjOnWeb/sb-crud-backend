package com.vijay.crudApi.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vijay.crudApi.Repo.ImageRepository;
import com.vijay.crudApi.Repo.ErrorLogService;
import com.vijay.crudApi.models.ImageEntity;
import com.vijay.crudApi.models.ImagesDTO;

@RestController
@CrossOrigin("http://localhost:3838")
@RequestMapping("/api/images")
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ErrorLogService errorLogService;

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            byte[] imageData = file.getBytes();
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImageData(imageData);
            imageRepository.save(imageEntity);

            log.info("Image uploaded successfully");
            errorLogService.logError(
                new Timestamp(System.currentTimeMillis()),
                "ImageController",
                "UPLOAD_IMAGE",
                "Image uploaded",
                Map.of("Size", String.valueOf(imageData.length))
            );

            return ResponseEntity.ok("Image uploaded successfully!");
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            errorLogService.logError(
                new Timestamp(System.currentTimeMillis()),
                "ImageController",
                "UPLOAD_IMAGE_FAIL",
                "Failed to upload image",
                Map.of("Error", e.getMessage())
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<ImageEntity> imageEntityOptional = imageRepository.findById(id);
        if (imageEntityOptional.isPresent()) {
            byte[] imageData = imageEntityOptional.get().getImageData();

            log.info("Fetched image with ID {}", id);
            errorLogService.logError(
                new Timestamp(System.currentTimeMillis()),
                "ImageController",
                "FETCH_IMAGE",
                "Fetched image",
                Map.of("ID", String.valueOf(id))
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            log.warn("Image not found with ID {}", id);
            errorLogService.logError(
                new Timestamp(System.currentTimeMillis()),
                "ImageController",
                "FETCH_IMAGE_FAIL",
                "Image not found",
                Map.of("ID", String.valueOf(id))
            );
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/all_images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ImagesDTO>> getAllImages() {
        try {
            List<ImageEntity> images = imageRepository.findAll();
            List<ImagesDTO> imageResponses = images.stream()
                .map(imageEntity -> new ImagesDTO(
                    imageEntity.getId(),
                    Base64.getEncoder().encodeToString(imageEntity.getImageData())))
                .collect(Collectors.toList());

            log.info("Fetched all images, count={}", imageResponses.size());
            errorLogService.logError(
                new Timestamp(System.currentTimeMillis()),
                "ImageController",
                "FETCH_ALL_IMAGES",
                "Fetched all images",
                Map.of("Count", String.valueOf(imageResponses.size()))
            );

            return new ResponseEntity<>(imageResponses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to fetch all images", e);
            errorLogService.logError(
                new Timestamp(System.currentTimeMillis()),
                "ImageController",
                "FETCH_ALL_IMAGES_FAIL",
                "Failed to fetch all images",
                Map.of("Error", e.getMessage())
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        try {
            Optional<ImageEntity> imageEntityOptional = imageRepository.findById(id);
            if (imageEntityOptional.isPresent()) {
                imageRepository.deleteById(id);
                log.info("Deleted image with ID {}", id);
                errorLogService.logError(
                    new Timestamp(System.currentTimeMillis()),
                    "ImageController",
                    "DELETE_IMAGE",
                    "Image deleted",
                    Map.of("ID", String.valueOf(id))
                );
                return ResponseEntity.ok("Image deleted successfully!");
            } else {
                log.warn("Image not found with ID {}", id);
                errorLogService.logError(
                    new Timestamp(System.currentTimeMillis()),
                    "ImageController",
                    "DELETE_IMAGE_FAIL",
                    "Image not found",
                    Map.of("ID", String.valueOf(id))
                );
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Failed to delete image with ID {}", id, e);
            errorLogService.logError(
                new Timestamp(System.currentTimeMillis()),
                "ImageController",
                "DELETE_IMAGE_ERROR",
                "Exception while deleting image",
                Map.of("ID", String.valueOf(id), "Error", e.getMessage())
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image.");
        }
    }
}
