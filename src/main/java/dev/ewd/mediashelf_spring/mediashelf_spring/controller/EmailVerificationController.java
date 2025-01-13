package dev.ewd.mediashelf_spring.mediashelf_spring.controller;

import dev.ewd.mediashelf_spring.mediashelf_spring.service.AuthService;
import dev.ewd.mediashelf_spring.mediashelf_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/verify")
public class EmailVerificationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Value("${app.cors.allowedOrigins}")
    private String frontendUrl;

    @GetMapping("/emailToken")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean verified = authService.verifyEmail(token);
        if(verified) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/verification-success"))
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/verification-failure"))
                    .build();
        }
    }

    @GetMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam("resetToken") String token ) {
        String username = userService.getUsernameByResetToken(token);
        String email = userService.getEmailByResetToken(token);
        boolean verified = authService.verifyResetPasswordEmail(token);
        if(verified) {
            String redirectUrl = String.format("%s/resetPassword?username=%s&email=%s",
                    frontendUrl,
                    username,
                    email
            );
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/resetPasswordFailed"))
                    .build();
        }
    }
}
