package com.vijay.crudApi.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.vijay.crudApi.Repo.ErrorLogService;
import com.vijay.crudApi.Repo.userRepo;
import com.vijay.crudApi.models.userRest;

@RestController
//@CrossOrigin("http://localhost:3838")
@CrossOrigin(origins = "${cors.allowed-origin}")
public class UserContollerApi {

    private static final Logger log = LoggerFactory.getLogger(UserContollerApi.class);

    @Autowired
    private userRepo userRepo;

    @Autowired
    private ErrorLogService errorLogService;

    @GetMapping("/")
    public String getData() {
        log.info("Frontend link requested");
        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),
            "UserContollerApi",
            "FRONTEND_LINK",
            "Frontend link accessed",
            Map.of("Path", "/")
        );
        return "<a target=\"_blank\" href=\"http://localhost:3838/spring_boot_react_crud\"> React Home Page Front End </a>";
    }

    @GetMapping("/api/users")
    public List<userRest> getUser() {
        List<userRest> users = userRepo.findAll();

        log.info("Fetched all users: count={}", users.size());
        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),
            "UserContollerApi",
            "FETCH_USERS",
            "Fetched user list",
            Map.of("Count", String.valueOf(users.size()))
        );

        return users;
    }

    @PostMapping("/api/save")
    public String saveUser(@RequestBody userRest user) {
        userRepo.save(user);

        log.info("User created: {}", user.getFirstName());
        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),
            "UserContollerApi",
            "CREATE_USER",
            "User created",
            Map.of("FirstName", user.getFirstName())
        );

        return "User Created";
    }

    @PutMapping("/api/update/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody userRest user) {
        userRest updateUser = userRepo.findById(id).orElse(null);
        if (updateUser == null) {
            log.warn("Update failed: User not found with ID {}", id);
            errorLogService.logError(
                new Timestamp(System.currentTimeMillis()),
                "UserContollerApi",
                "UPDATE_USER_FAIL",
                "User not found",
                Map.of("ID", String.valueOf(id))
            );
            return "User not found!";
        }

        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setAge(user.getAge());
        updateUser.setOccupation(user.getOccupation());

        userRepo.save(updateUser);

        log.info("User updated: ID {}", id);
        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),
            "UserContollerApi",
            "UPDATE_USER",
            "User updated",
            Map.of("ID", String.valueOf(id))
        );

        return "Updated the user.";
    }

    @DeleteMapping("/api/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRest deleteUser = userRepo.findById(id).orElse(null);
        if (deleteUser == null) {
            log.warn("Delete failed: User not found with ID {}", id);
            errorLogService.logError(
                new Timestamp(System.currentTimeMillis()),
                "UserContollerApi",
                "DELETE_USER_FAIL",
                "User not found",
                Map.of("ID", String.valueOf(id))
            );
            return "User not found!";
        }

        userRepo.delete(deleteUser);

        log.info("User deleted: ID {}", id);
        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),
            "UserContollerApi",
            "DELETE_USER",
            "User deleted",
            Map.of("ID", String.valueOf(id))
        );

        return "Deleted the user: " + id;
    }
}
