package org.example.backend.service;


import org.example.backend.domain.AbstractUser;
import org.example.backend.domain.User;

import java.util.List;

public interface AbstractUserService {
    AbstractUser checkUser(String username, String password);

    boolean saveUser(AbstractUser abstractUser);

    boolean deleteUserById(long id);

    List<AbstractUser> findAllUser();

    AbstractUser findUserById(long id);

    AbstractUser findUserByUsername(String username);

    List<User> findAllUsers();
}
