package org.wit.hrmatching.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.service.auth.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterDTO dto) {
        authService.registerUser(dto);
        return ResponseEntity.ok("User registered successfully");
    }
}
