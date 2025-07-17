package com.vijay.crudApi.Repo;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.crudApi.models.AppUsers;

public interface AppUserRepository extends JpaRepository<AppUsers, Long> {
    Optional<AppUsers> findByEmail(String email);
    
    boolean existsByEmail(String email); // ✅ Added method
}
