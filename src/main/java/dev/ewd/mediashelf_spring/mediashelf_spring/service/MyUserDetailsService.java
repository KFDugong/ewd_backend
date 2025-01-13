package dev.ewd.mediashelf_spring.mediashelf_spring.service;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.UserPrincipal;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.Users;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!user.isEnabled()) {
            throw new UsernameNotFoundException("User is not enabled. Please verify your email");
        }
        return new UserPrincipal(user);
    }

    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException{
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!user.isEnabled()) {
            throw new UsernameNotFoundException("User is not enabled. Please verify your email");
        }
        return new UserPrincipal(user);
    }
}
