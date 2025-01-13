package dev.ewd.mediashelf_spring.mediashelf_spring.service;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.updateDto.UpdateUserDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.Users;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.VerificationToken;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.enums.UserRole;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.UserRepository;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.VerificationTokenRepository;
import dev.ewd.mediashelf_spring.mediashelf_spring.util.AuthUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthService authService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Map<String, Object> updateUser(UpdateUserDto updatedUser) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean updateUsernameField = updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty();
        boolean updateEmailField = updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty();
        boolean updatePasswordField = updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty();

        boolean usernameExists = updateUsernameField && userRepository.findByUsername(updatedUser.getUsername()).isPresent();
        if(usernameExists) throw new RuntimeException("Username is already taken.");
        boolean emailExists = updateEmailField && userRepository.findByEmail(updatedUser.getEmail()).isPresent();
        if(emailExists) throw new RuntimeException("Email is already taken.");


        if(updateUsernameField) existingUser.setUsername(updatedUser.getUsername());
        if(updateEmailField) existingUser.setEmail(updatedUser.getEmail());
        if(updatePasswordField) existingUser.setPassword(encoder.encode(updatedUser.getPassword()));

        Users savedUser = userRepository.save(existingUser);
        SecurityContextHolder.clearContext();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profile updated successfully. Please log in again.");
        return response;
    }

    public void deleteCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public Map<String, String> getUserProfile() {
        Long currentUserId = AuthUtil.getCurrentUserId();
        Users user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        String username = user.getUsername();
        String email = user.getEmail();

        Map<String, String> profile = new HashMap<>();
        profile.put("username", username);
        profile.put("email", email);

        return profile;
    }

    public void handleForgotPassword(String email) throws MessagingException {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String resetToken = authService.generateVerificationToken(user);
        String resetPasswordLink = baseUrl + "/api/verify/resetPassword?resetToken=" + resetToken;
        try {
            emailService.sendPasswordResetEmail(email, resetPasswordLink);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Users getUserProfileByResetToken (String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new RuntimeException("Reset token invalid.");
        }
        Users user = verificationToken.getUser();
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    public String getUsernameByResetToken(String token) {
        return getUserProfileByResetToken(token).getUsername();
    }

    public String getEmailByResetToken(String token) {
        return getUserProfileByResetToken(token).getEmail();
    }

    public Map<String, String> makeUserAdmin(String username, String email) throws MessagingException {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if(!user.getEmail().equals(email)) {
            throw new RuntimeException("The email does not match the username.");
        }

        user.setUserRole(UserRole.ADMIN);
        userRepository.save(user);
        emailService.sendApprovalMessage(email, username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User '" + username + "'has been granted admin privileges.");
        return response;
    }
}
