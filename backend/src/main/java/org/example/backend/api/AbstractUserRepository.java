package org.example.backend.api;

import org.example.backend.domain.AbstractUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractUserRepository extends JpaRepository<AbstractUser, Long> {
    // 使用标准的JPA方法，返回Optional
    // AbstractUser findById(long id); // 移除自定义方法，使用JPA标准方法

    AbstractUser findByUsername(String username);

}
