package dev.ewd.mediashelf_spring.mediashelf_spring.util;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUserId();
        } else {
            throw new IllegalStateException("Authentication principal is not an instance of UserPrincipal");
        }

    }
}
