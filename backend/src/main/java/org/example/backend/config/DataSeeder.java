package org.example.backend.config;

import org.example.backend.domain.*;
import org.example.backend.service.AbstractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据种子类 - 用于初始化测试数据
 * 根据教授反馈，需要为不同用户组提供数据种子
 * @author Generated
 * @version 1.0
 */
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AbstractUserService abstractUserService;

    // 使用相对路径，避免硬编码IP地址导致的连接超时
    private final String IMAGE_PATH = "/image/";

    @Override
    public void run(String... args) {
        try {
            // 检查是否已有数据，避免重复初始化
            // 对于持久化数据库，只在首次启动时初始化数据
            List<AbstractUser> existingUsers = abstractUserService.findAllUser();
            if (existingUsers == null || existingUsers.isEmpty()) {
                System.out.println("数据库为空，开始初始化种子数据...");
                seedData();
                System.out.println("种子数据初始化完成");
            } else {
                System.out.println("数据库中已有 " + existingUsers.size() + " 个用户，跳过种子数据初始化");
            }
        } catch (Exception e) {
            // 如果数据库连接失败，记录错误但不阻止应用启动
            System.err.println("数据种子初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 初始化种子数据
     */
    private void seedData() {
        // 创建管理员
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setName("系统管理员");
        admin.setBio("系统管理员账户");
        abstractUserService.saveUser(admin);

        // 创建普通用户（不同权限组）
        createUsersWithPermissions();
    }

    /**
     * 创建具有不同角色的基础测试用户
     * - 工作人员（Organizers）：可以创建活动 / 报名 / 评论
     * - 参与者（Participants）：可以报名 / 评论，但不能创建活动
     */
    private void createUsersWithPermissions() {
        // 工作人员账号（可发布活动）
        createUser("staff_alice", "staff_alice", "Alice (Staff)", true, true, true);
        createUser("staff_bob", "staff_bob", "Bob (Staff)", true, true, true);

        // 参与者账号（只能报名和评论）
        createUser("participant_chris", "participant_chris", "Chris (Participant)", false, true, true);
        createUser("participant_diana", "participant_diana", "Diana (Participant)", false, true, true);
        createUser("participant_eli", "participant_eli", "Eli (Participant)", false, true, true);
    }

    /**
     * 创建用户并设置权限
     */
    private User createUser(String username, String password, String name, 
                           boolean canCreate, boolean canEnroll, boolean canComment) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setBio("这是" + name + "的个人简介");
        user.setAvatar(IMAGE_PATH + "default_avatar.jpg");
        
        Permission permission = new Permission();
        permission.setUser(user);
        permission.setCanCreate(canCreate);
        permission.setCanEnroll(canEnroll);
        permission.setCanComment(canComment);
        user.setPermission(permission);
        
        abstractUserService.saveUser(user);
        return user;
    }
}

