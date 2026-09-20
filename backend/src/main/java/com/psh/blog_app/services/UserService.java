package com.psh.blog_app.services;

import com.psh.blog_app.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
