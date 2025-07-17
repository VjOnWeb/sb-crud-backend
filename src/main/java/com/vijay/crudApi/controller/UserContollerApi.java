package com.vijay.crudApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.crudApi.Repo.userRepo;
import com.vijay.crudApi.models.userRest;

@RestController
@CrossOrigin("http://localhost:3838")
public class UserContollerApi {
    @Autowired
    private userRepo userRepo;

    @GetMapping("/")
    public String getData() {
        return "<a  target=\"_blank\"  href=\"http://localhost:3838/spring_boot_react_crud\"> React Home Page Front End </a>";
    }

    @GetMapping(value = "/api/users")

    public List<userRest> getUser() {
        return userRepo.findAll();
    }

    @SuppressWarnings("null")

    @PostMapping(value = "/api/save")
    public String saveUser(@RequestBody userRest user) {
        userRepo.save(user);
        return "User Created ";
    }

    @SuppressWarnings("null")

    @PutMapping(value = "/api/update/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody userRest user) {
        userRest updateUser = userRepo.findById(id).get();
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setAge(user.getAge());
        updateUser.setOccupation(user.getOccupation());
        userRepo.save(updateUser);

        return "Update the user..";
    }

    @SuppressWarnings("null")
    @DeleteMapping(value = "/api/delete/{id}")

    public String deleteUser(@PathVariable Long id) {
        userRest deleteUser = userRepo.findById(id).get();
        userRepo.delete(deleteUser);

        return " Deleted the user : " + id;
    }
}
