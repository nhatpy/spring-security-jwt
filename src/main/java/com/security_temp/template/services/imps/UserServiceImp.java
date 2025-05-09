package com.security_temp.template.services.imps;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security_temp.template.models.User;
import com.security_temp.template.repositories.UserRepository;
import com.security_temp.template.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) {
        Optional<User> authenticatedUser = userRepository.findByUsername(username);

        if (authenticatedUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            return authenticatedUser.get();
        }
    }

}
