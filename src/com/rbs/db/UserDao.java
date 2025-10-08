package com.rbs.db;

import com.rbs.model.User;

public interface UserDao {
    // Placeholder methods; implement with JDBC SQL in an implementation class
    User findByUsername(String username);
    boolean create(User user);
}



