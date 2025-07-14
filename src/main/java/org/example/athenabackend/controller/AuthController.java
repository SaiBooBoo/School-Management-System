package org.example.athenabackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.athenabackend.model.Gender;
import org.example.athenabackend.model.Subject;
import org.example.athenabackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
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
                                  String image_url,
                                  Subject subject
                                  ) {}
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        String returnString = authService.register(
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
                registerRequest.image_url,
                registerRequest.subject);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnString);
    }
}
