package com.TGDCVS.serviceImpl;

import com.TGDCVS.service.AuthService;
import com.TGDCVS.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Override
    public String login(String username, String password) {

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            if (auth.isAuthenticated()) {
                return jwtUtil.generateToken(username);
            }

            throw new RuntimeException("Authentication failed");

        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}