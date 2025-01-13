package dev.ewd.mediashelf_spring.mediashelf_spring.service;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.LoginRequest;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.createDto.RegisterUserDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.exception.WrongCredentialsException;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.AuthResponse;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.Users;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.VerificationToken;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.enums.MediaCollectionType;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.UserRepository;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    MediaCollectionService mediaCollectionService;

    @Autowired
    EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(RegisterUserDto registerUserDto){

        if(userRepository.findByUsername(registerUserDto.getUsername()).isPresent())
            throw new WrongCredentialsException("Username already exists");
        if(userRepository.findByEmail(registerUserDto.getEmail()).isPresent())
            throw new WrongCredentialsException("Email already exists");

        Users user = new Users();
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(encoder.encode(registerUserDto.getPassword()));
        user.setEnabled(false);

        Users savedUser = userRepository.save(user);
        mediaCollectionService.createMediaCollection("Watch", "Default watched collection", MediaCollectionType.WATCHED, savedUser.getId());
        mediaCollectionService.createMediaCollection("To watch", "Default to-watch collection", MediaCollectionType.TO_WATCH, savedUser.getId());

        String token = generateVerificationToken(savedUser);
        String verificationLink = baseUrl + "/api/verify/emailToken?token=" + token;
        try {
            emailService.sendVerificationEmail(savedUser.getEmail(), "Verify Your Email", verificationLink);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return savedUser;
    }

    public AuthResponse verify(LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );

            Users authenticatedUser = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found."));

            if(!authenticatedUser.isEnabled()) {
                throw new RuntimeException("Please verify your email first");
            }

            String accessToken = jwtService.generateAccessToken(authenticatedUser);
            String refreshToken = jwtService.generateRefreshToken(authenticatedUser);
            return new AuthResponse(accessToken, refreshToken, authenticatedUser.getUsername(), authenticatedUser.getEmail());
        } catch (BadCredentialsException e){
            throw new WrongCredentialsException("Wrong credentials provided");
        }
    }

    public AuthResponse refreshToken(String refreshToken){
        Long userId = jwtService.extractUserId(refreshToken);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String newAccessToken = jwtService.generateAccessToken(user);
        return new AuthResponse(newAccessToken, refreshToken, user.getUsername(), user.getEmail());
    }

    public String generateVerificationToken(Users user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(
                token,
                user,
                LocalDateTime.now().plusHours(1)
        );

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public boolean verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        Users user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);
        return true;
    }

    public boolean verifyResetPasswordEmail (String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        verificationTokenRepository.delete(verificationToken);
        return true;
    }

    public Map<String, Object> resetPassword(RegisterUserDto dto) {
        userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("Email not found."));
        Users user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new RuntimeException("User not found."));
        user.setPassword(encoder.encode(dto.getPassword()));
        userRepository.save(user);
        SecurityContextHolder.clearContext();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profile updated successfully. Please log in again.");
        return response;
    }
}
