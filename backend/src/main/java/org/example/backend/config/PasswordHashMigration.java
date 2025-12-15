package org.example.backend.config;

import jakarta.persistence.EntityManager;
import org.example.backend.api.AbstractUserRepository;
import org.example.backend.domain.AbstractUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PasswordHashMigration implements CommandLineRunner {

    @Autowired
    private AbstractUserRepository abstractUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;

    private boolean isBCrypt(String pwd) {
        return pwd != null && (pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$"));
    }

    @Override
    @Transactional
    public void run(String... args) {
        List<AbstractUser> users = abstractUserRepository.findAll();
        boolean updated = false;
        for (AbstractUser user : users) {
            String pwd = user.getPassword();
            if (pwd != null && !isBCrypt(pwd)) {
                user.setPassword(passwordEncoder.encode(pwd));
                updated = true;
            }
        }
        if (updated) {
            abstractUserRepository.saveAll(users);
            entityManager.flush();
            entityManager.clear();
        }
    }
}


