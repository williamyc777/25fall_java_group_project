package org.example.backend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.backend.api.AbstractUserRepository;
import org.example.backend.api.UserRepository;
import org.example.backend.domain.AbstractUser;
import org.example.backend.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AbstractUserServiceImpl implements AbstractUserService {
    @Autowired
    private AbstractUserRepository abstractUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
//    @Autowired
//    JedisService jedisService;

    @Override
    public AbstractUser checkUser(String username, String password) {
        AbstractUser user = abstractUserRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return passwordEncoder.matches(password, user.getPassword()) ? user : null;
    }

    @Override
    @Transactional
    public boolean saveUser(AbstractUser abstractUser) {
        System.out.println("saveUser - Before save, avatar: " + abstractUser.getAvatar());
        if (abstractUser.getPassword() != null) {
            String pwd = abstractUser.getPassword();
            // avoid double-encoding if already BCrypt
            if (!(pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$"))) {
                abstractUser.setPassword(passwordEncoder.encode(pwd));
            }
        }
        AbstractUser saved = abstractUserRepository.save(abstractUser);
        // 立即刷新到数据库，确保数据已持久化
        entityManager.flush();
        System.out.println("saveUser - After flush, saved avatar: " + saved.getAvatar());
        // 清除EntityManager中的所有缓存，确保下次查询时获取最新数据
        entityManager.clear();
        System.out.println("saveUser - Cache cleared");
        return true;
    }

    @Override
    public boolean deleteUserById(long id) {
        return true;
    }

    @Override
    public List<AbstractUser> findAllUser() {
        return abstractUserRepository.findAll();
    }

    @Override
    public AbstractUser findUserById(long id) {
        // 清除EntityManager缓存，确保获取最新数据
        entityManager.clear();
        // 使用EntityManager的find方法，绕过Repository缓存
        AbstractUser user = entityManager.find(AbstractUser.class, id);
        System.out.println("findUserById(" + id + ") - avatar: " + (user != null ? user.getAvatar() : "null"));
        return user;
    }

    @Override
    public AbstractUser findUserByUsername(String username) {
        return abstractUserRepository.findByUsername(username);
    }

    @Override
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
