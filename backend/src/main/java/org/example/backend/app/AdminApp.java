package org.example.backend.app;

import org.example.backend.config.MyException;
import org.example.backend.domain.AbstractUser;
import org.example.backend.domain.Admin;
import org.example.backend.domain.Permission;
import org.example.backend.domain.User;
import org.example.backend.dto.UserDto;
import org.example.backend.service.*;
import org.example.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 该类用于处理管理员相关的请求
 * @author bgamard
 * @version 1.0
 */

@RestController
@RequestMapping("/admin")
public class AdminApp {
    @Autowired
    AbstractUserService abstractUserService;
    @Autowired
    EventService eventService;
    @Autowired
    EventCommentService eventCommentService;
    @Autowired
    ReplyCommentService replyCommentService;
    @Autowired
    PostService postService;

    /**
     * 验证管理员权限
     * @param token JWT token
     * @throws MyException 如果不是管理员则抛出异常
     */
    private void verifyAdminPermission(String token) {
        if (token == null || token.isEmpty()) {
            throw new MyException(1, "Token is required");
        }
        AbstractUser user = JwtUtil.verifyToken(token);
        if (user == null) {
            throw new MyException(2, "Invalid token");
        }
        if (!(user instanceof Admin)) {
            throw new MyException(3, "Admin permission required");
        }
    }

    /**
     * @return 返回所有用户的信息
     */
    @GetMapping("/user")
    public List<UserDto> getAllUsers(@RequestHeader("Authorization") String token) {
        verifyAdminPermission(token);
        return abstractUserService.findAllUsers().stream().map(UserDto::new).toList();
    }

    /**
     * @param userId 用户id
     * @return 返回一个boolean值表示权限是否修改成功
     */
    @PostMapping("/permission")
    public boolean changePermission(@RequestHeader("Authorization") String token, long userId, @RequestParam("canCreate") boolean canCreate, @RequestParam("canEnroll") boolean canEnroll, @RequestParam("canComment") boolean canComment) {
        verifyAdminPermission(token);
        User user = (User) abstractUserService.findUserById(userId);
        if (user == null) {
            throw new MyException(-1, "User not found");
        }
        Permission permission = user.getPermission();
        if (permission == null) {
            permission = new Permission();
            permission.setUser(user);
        }
        permission.setCanCreate(canCreate);
        permission.setCanEnroll(canEnroll);
        permission.setCanComment(canComment);
        abstractUserService.saveUser(user);
        return true;
    }

    /**
     * @return 返回一个boolean值表示活动是否删除成功
     */
    @DeleteMapping("/event")
    public boolean deleteEvent(@RequestHeader("Authorization") String token, @RequestParam("eventId") long eventId) {
        verifyAdminPermission(token);
        return eventService.deleteEvent(eventId);
    }

    /**
     * @return 返回一个boolean值表示评论是否删除成功
     */
    @DeleteMapping("/eventComment")
    public boolean deleteEventComment(@RequestHeader("Authorization") String token, long commentId) {
        verifyAdminPermission(token);
        return eventCommentService.deleteEventComment(commentId);
    }

    /**
     * @return 返回一个boolean值表示回复评论是否删除成功
     */
    @DeleteMapping("/replyComment")
    public boolean deleteReplyComment(@RequestHeader("Authorization") String token, long commentId) {
        verifyAdminPermission(token);
        return replyCommentService.deleteReplyComment(commentId);
    }

    /**
     * @return 返回一个boolean值表示帖子是否删除成功
     */
    @DeleteMapping("/post")
    public boolean deletePost(@RequestHeader("Authorization") String token, @RequestParam("postId") long postId) {
        verifyAdminPermission(token);
        return postService.deletePost(postId);
    }

    /**
     * @return 返回所有活动信息（用于管理员界面）
     */
    @GetMapping("/event")
    @org.springframework.transaction.annotation.Transactional
    public List<org.example.backend.dto.EventBriefDto> getAllEvents(@RequestHeader("Authorization") String token) {
        verifyAdminPermission(token);
        List<org.example.backend.domain.Event> events = eventService.findAllEvents();
        return events.stream()
                .map(event -> {
                    // 确保在事务内初始化懒加载属性
                    if (event.getAuthor() != null) {
                        event.getAuthor().getId();
                        event.getAuthor().getName();
                    }
                    if (event.getAbstractEnrollment() != null) {
                        event.getAbstractEnrollment().getStartTime();
                        event.getAbstractEnrollment().getEndTime();
                    }
                    return new org.example.backend.dto.EventBriefDto(event);
                })
                .toList();
    }

    /**
     * @return 返回所有帖子信息（用于管理员界面）
     */
    @GetMapping("/post")
    @org.springframework.transaction.annotation.Transactional
    public List<org.example.backend.dto.PostDto> getAllPosts(@RequestHeader("Authorization") String token) {
        verifyAdminPermission(token);
        List<org.example.backend.domain.Post> posts = postService.findAllPosts();
        return posts.stream()
                .map(post -> {
                    // 确保在事务内初始化懒加载属性
                    if (post.getUser() != null) {
                        post.getUser().getId();
                        post.getUser().getName();
                        post.getUser().getUsername();
                    }
                    if (post.getEvent() != null) {
                        post.getEvent().getId();
                    }
                    if (post.getLikeUsers() != null) {
                        post.getLikeUsers().size();
                    }
                    org.example.backend.dto.PostDto dto = new org.example.backend.dto.PostDto(post);
                    // 设置评论数
                    dto.setPostCommentAmount(eventCommentService.findEventCommentByPostId(post.getId()).size());
                    return dto;
                })
                .toList();
    }

}
