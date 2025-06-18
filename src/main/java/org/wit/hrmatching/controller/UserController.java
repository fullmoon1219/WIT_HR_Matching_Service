package org.wit.hrmatching.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wit.hrmatching.dto.login.UserLoginDTO;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.service.UserService;
import org.wit.hrmatching.vo.UserVO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDTO dto, HttpSession session) {
        UserVO user = userService.login(dto);

        session.setAttribute("loginUser", user);

        return ResponseEntity.ok("User logged in successfully");
    }

}
