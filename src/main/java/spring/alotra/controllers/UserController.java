package spring.alotra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import spring.alotra.entity.UserEntity;
import spring.alotra.service.ServiceImpl;
import spring.alotra.service.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private ServiceImpl service;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserEntity user) {
        return ResponseEntity.ok( service.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(service.login(username, password));
    }
}
