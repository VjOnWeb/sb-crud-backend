package com.vijay.crudApi.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.crudApi.models.ImageEntity;


public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    // Custom query methods, if needed
}
