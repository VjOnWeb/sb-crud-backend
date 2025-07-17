package com.vijay.crudApi.models;

public class AuthRequest {


 private String email;
 private String password;

 // Getters & Setters
 public String getEmail() {
     return email.toLowerCase(); // 🔥 Convert to lowercase always
     }
 public void setEmail(String email) {
     this.email = email;
 }

 public String getPassword() {
     return password;
 }
 public void setPassword(String password) {
     this.password = password;
 }
}
