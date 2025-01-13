package dev.ewd.mediashelf_spring.mediashelf_spring.controller;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.LoginRequest;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.RefreshTokenRequest;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.createDto.RegisterUserDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.exception.UnexpectedErrorException;
import dev.ewd.mediashelf_spring.mediashelf_spring.exception.WrongCredentialsException;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.AuthResponse;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.Users;
import dev.ewd.mediashelf_spring.mediashelf_spring.service.AuthService;
import dev.ewd.mediashelf_spring.mediashelf_spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        Users registeredUser = authService.register(registerUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            AuthResponse response = authService.verify(loginRequest);
            return ResponseEntity.ok(response);
        } catch (WrongCredentialsException e){
            throw new WrongCredentialsException(e.getMessage());
        } catch (Exception e){
            throw new UnexpectedErrorException("An unexpected error occured during logging.");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        try {
            AuthResponse response = authService.refreshToken(refreshTokenRequest.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (Exception e){
            throw new UnexpectedErrorException("Failed to refresh token.");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody RegisterUserDto dto) {
        try {
            Map<String, Object> response = authService.resetPassword(dto);
            return ResponseEntity.ok(response);
        } catch (Exception e){
            throw new UnexpectedErrorException("Failed to reset password.");
        }
    }
}
