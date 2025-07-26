package org.example.athenabackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.dao.UserDao;
import org.example.athenabackend.entity.Role;
import org.example.athenabackend.model.Gender;
import org.example.athenabackend.model.Subject;
import org.example.athenabackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserDao userDao;
    public record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String returnString = authService.login(loginRequest.username, loginRequest.password);

        return ResponseEntity.ok(returnString);
    }

    /*
        private String qualification;
    private BigDecimal earning;
     */
    public record RegisterRequest(String username,
                                  String password,
                                  String nrcNumber,
                                  String displayName,
                                  Gender gender,
                                  LocalDate dob,
                                  String address,
                                  String phoneNumber,
                                  String job,
                                  String parentType,
                                  String qualification,
                                  String accountType,
                                  String profileImagePath,
                                  Subject subject,
                                  BigDecimal grade,
                                  Role roles
                                  ) {}

    public record RegisterResponse(String msg, Integer id){}

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        Integer id = authService.register(
                registerRequest.username,
                registerRequest.password,
                registerRequest.nrcNumber,
                registerRequest.phoneNumber,
                registerRequest.dob,
                registerRequest.displayName,
                registerRequest.gender,
                registerRequest.address,
                registerRequest.job,
                registerRequest.parentType,
                registerRequest.qualification,
                registerRequest.accountType,
                registerRequest.profileImagePath,
                registerRequest.subject,
                registerRequest.grade,
                registerRequest.roles);
        String msg = "Account created successfully with ID " + id;
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(msg, id));
    }

    @GetMapping("/role")
    public ResponseEntity<Role> findRoleByName(@PathVariable String roleName){
        Role role = authService.findRoleByName(roleName);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @GetMapping("/accountType/{username}")
    public ResponseEntity<String> getAccountTypeByUsername(@PathVariable String username){
        String accountType = authService.findAccountTypeByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(accountType);
    }
}
