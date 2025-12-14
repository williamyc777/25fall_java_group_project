package org.example.backend.app;

import org.example.backend.config.MyException;
import org.example.backend.domain.AbstractUser;
import org.example.backend.domain.Admin;
import org.example.backend.domain.Permission;
import org.example.backend.domain.User;
import org.example.backend.dto.AbstractUserDto;
import org.example.backend.service.AbstractUserService;
import org.example.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;

/**
 * 该类用于处理用户登录注册相关的请求
 * @author Shinomiya
 * @version 1.0
 */
@RestController
public class AbstractUserApp {
    private final AbstractUserService abstractUserService;
    private final ResourceUrlProvider mvcResourceUrlProvider;
    // 使用相对路径，避免硬编码IP地址导致的连接超时
    private final String IMAGE_PATH = "/image/";

    @Autowired
    public AbstractUserApp(AbstractUserService abstractUserService, ResourceUrlProvider mvcResourceUrlProvider) {
        this.abstractUserService = abstractUserService;
        this.mvcResourceUrlProvider = mvcResourceUrlProvider;
    }

    /**
     * @param username 用户名
     * @param password 密码
     * @return 返回一个token
     */
    @PostMapping(value = "/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        AbstractUser user = abstractUserService.checkUser(username, password);
        if (user == null) {
            throw new MyException(4, "incorrect username or password");
        }
        System.out.println(user.getId());
        return JwtUtil.getToken(String.valueOf(user.getId()), password);
    }

    /**
     * @param username 用户名
     * @param password 密码
     * @return 返回一个boolean值表示是否注册成功
     */
    @PostMapping("/signUp")
    public boolean signUp(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        if (abstractUserService.findUserByUsername(username) != null) {
            throw new MyException(5, "username already exists");
        }
        //test sign up function
        System.out.println("Received signUp request → username: " + username + ", password: " + password);

        user.setUsername(username);
        user.setPassword(password);
        user.setName(username);
        user.setAvatar(IMAGE_PATH + "default_avatar.jpg");
        Permission permission = new Permission();
        permission.setUser(user);
        user.setPermission(permission);
        permission.setCanCreate(false);
        permission.setCanEnroll(true);
        permission.setCanComment(true);
        try {
            abstractUserService.saveUser(user);
            return true;
        } catch (Exception e) {
            throw new MyException(6, "sign up failed");
        }
    }

    /**
     *
     * @param token 用户对应的token
     * @return 用户信息
     */
    @GetMapping("/userInfo")
    public AbstractUserDto getUserByToken(@RequestHeader("Authorization") String token) {
        AbstractUser user = JwtUtil.verifyToken(token);
        if(user == null) {
            throw new MyException(7, "token invalid");
        }
        return new AbstractUserDto(user);
    }
}
