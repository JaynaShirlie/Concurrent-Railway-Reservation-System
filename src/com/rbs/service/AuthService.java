package com.rbs.service;

import com.rbs.db.UserDao;
import com.rbs.model.User;

public class AuthService {
    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean register(User user, String passwordPlain) {
        // Placeholder: hash = passwordPlain (replace with hashing)
        user.setPasswordHash(passwordPlain);
        return userDao.create(user);
    }

    public User login(String username, String passwordPlain) {
        User u = userDao.findByUsername(username);
        if (u == null) return null;
        return passwordPlain.equals(u.getPasswordHash()) ? u : null;
    }
}



