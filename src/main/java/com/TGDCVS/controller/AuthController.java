package com.TGDCVS.controller;

import com.TGDCVS.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        System.out.println("LOGIN API HIT");
        return authService.login(req.getUsername(), req.getPassword());
    }

    @Getter
    @Setter
    static class LoginRequest {
        private String username;
        private String password;
    }
}