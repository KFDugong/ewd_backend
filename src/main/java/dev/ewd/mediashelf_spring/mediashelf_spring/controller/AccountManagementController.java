package dev.ewd.mediashelf_spring.mediashelf_spring.controller;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.PasswordResetRequest;
import dev.ewd.mediashelf_spring.mediashelf_spring.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountManagementController {

    @Autowired
    private UserService userService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequest request) throws MessagingException {
        userService.handleForgotPassword(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Email has been sent."));
    }

    @PostMapping("/makeAdmin")
    public ResponseEntity<?> makeUserAdmin(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");

        if(username == null || email == null) {
            return ResponseEntity.badRequest().body("(Username and email are required.");
        }
        try {
            Map<String, String> response = userService.makeUserAdmin(username, email);
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send notification email.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
