package dev.ewd.mediashelf_spring.mediashelf_spring.controller;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.updateDto.UpdateUserDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.exception.UnexpectedErrorException;
import dev.ewd.mediashelf_spring.mediashelf_spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getUserProfile() {
        try {
            Map<String, String> userProfile = userService.getUserProfile();
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            throw new UnexpectedErrorException("An unexpected error occurred while fetching your profile");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UpdateUserDto updatedUser){
        try{
            Map<String, Object> response = userService.updateUser(updatedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new UnexpectedErrorException("An unexpected error while updating you");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(){
        try {
            userService.deleteCurrentUser();
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            throw new UnexpectedErrorException("An unexpected error happened while deleting your profile");
        }
    }
}
