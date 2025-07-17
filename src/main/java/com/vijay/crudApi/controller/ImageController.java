package com.vijay.crudApi.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vijay.crudApi.Repo.ImageRepository;
import com.vijay.crudApi.models.ImageEntity;
import com.vijay.crudApi.models.ImagesDTO;

@RestController
@CrossOrigin("http://localhost:3838")
@RequestMapping("/api/images")
//@CrossOrigin(origins = "*") // <-- This enables frontend communication for all dev ports

public class ImageController {

  @Autowired
  private ImageRepository imageRepository;

  @PostMapping("/uploadImage")
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    // Your upload logic here
    try {
      // Convert the MultipartFile to a byte array
      byte[] imageData = file.getBytes();

      // Save the image data to the MySQL database
      ImageEntity imageEntity = new ImageEntity();
      imageEntity.setImageData(imageData);
      imageRepository.save(imageEntity);

      return ResponseEntity.ok("Image uploaded successfully!");
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
    }
  }

  @SuppressWarnings("null")

  @GetMapping("/{id}")
  public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
    Optional<ImageEntity> imageEntityOptional = imageRepository.findById(id);
    if (imageEntityOptional.isPresent()) {
      // Retrieve image data from the MySQL database
      byte[] imageData = imageEntityOptional.get().getImageData();

      // Return the image data in the response
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type
      return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
//
//  @GetMapping(value = "/all_images", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<List<ImagesDTO>> getAllImages() {
//    try {
//      List<ImageEntity> images = imageRepository.findAll();
//
//      // Convert ImageEntity to ImageResponse containing ID and base64 data
//      List<ImagesDTO> imageResponses = images.stream()
//          .map(imageEntity -> new ImagesDTO(imageEntity.getId(),
//              Base64.getEncoder().encodeToString(imageEntity.getImageData())))
//          .collect(Collectors.toList());
//      System.out.println("imageResponses Data and ID " + imageResponses);
//      return new ResponseEntity<>(imageResponses, HttpStatus.OK);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
//    }
//  }

  

@GetMapping(value = "/all_images", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<List<ImagesDTO>> getAllImages() {
    try {
        List<ImageEntity> images = imageRepository.findAll();

        // Convert ImageEntity to ImagesDTO, with base64 encoding of the image data
        List<ImagesDTO> imageResponses = images.stream()
            .map(imageEntity -> new ImagesDTO(
                imageEntity.getId(),
                Base64.getEncoder().encodeToString(imageEntity.getImageData()) // Base64 encoding of image data
            ))
            .collect(Collectors.toList());

        return new ResponseEntity<>(imageResponses, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
    }
}


  
  @SuppressWarnings("null")
  
  

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteImage(@PathVariable Long id) {
    try {
      // Check if the image exists
      Optional<ImageEntity> imageEntityOptional = imageRepository.findById(id);
      if (imageEntityOptional.isPresent()) {
        // Delete the image from the MySQL database
        imageRepository.deleteById(id);
        return ResponseEntity.ok("Image deleted successfully!");
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image.");
    }
  }
}
