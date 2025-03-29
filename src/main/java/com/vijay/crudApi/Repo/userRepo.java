package com.vijay.crudApi.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.crudApi.models.userRest;

public interface userRepo extends JpaRepository<userRest, Long> {

}
