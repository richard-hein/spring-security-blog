package com.psh.blog_app.services.impl;

import com.psh.blog_app.domain.entities.User;
import com.psh.blog_app.repositories.UserRepository;
import com.psh.blog_app.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: "+ id));
    }
}
