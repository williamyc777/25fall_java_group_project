package org.example.backend.app;


import org.example.backend.config.MyException;
import org.example.backend.domain.AbstractUser;
import org.example.backend.domain.Post;
import org.example.backend.domain.User;
import org.example.backend.dto.AbstractUserDto;
import org.example.backend.dto.PostDto;
import org.example.backend.service.AbstractUserService;
import org.example.backend.service.EventCommentService;
import org.example.backend.service.PostService;
import org.example.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类用于处理用户个人信息相关的请求
 * @author Shinomiya
 * @version 1.0
 */
@RestController
@RequestMapping("/profile")
public class ProfileApp {
    @Autowired
    private AbstractUserService abstractUserService;
    @Autowired
    private PostService postService;
    @Autowired
    private EventCommentService eventCommentService;
    @Autowired
    private org.example.backend.service.EventService eventService;

    /**
     * 编辑用户个人信息
     * @param token 用户token
     * @param name 用户昵称
     * @param bio 用户简介
     * @return boolean
     */
    @PostMapping("/info/edit")
    public boolean editProfile(@RequestHeader("Authorization") String token, @RequestParam("name") String name, @RequestParam("bio") String bio){
        AbstractUser user = JwtUtil.verifyToken(token);
        if(user == null){
            throw new MyException(0, "Invalid token");
        }
        user.setName(name);
        user.setBio(bio);
        abstractUserService.saveUser(user);
        return true;
    }

    /**
     * 编辑用户头像
     * @param token 用户token
     * @param avatar 用户头像
     * @return boolean
     */
    @PostMapping("/avatar/edit")
    public boolean editAvatar(@RequestHeader("Authorization") String token, @RequestParam("avatar") String avatar){
        AbstractUser user = JwtUtil.verifyToken(token);
        if(user == null){
            throw new MyException(0, "Invalid token");
        }
        System.out.println("=== Avatar Update Start ===");
        System.out.println("User ID: " + user.getId());
        System.out.println("User username: " + user.getUsername());
        System.out.println("Current avatar: " + user.getAvatar());
        System.out.println("New avatar URL: " + avatar);
        
        user.setAvatar(avatar);
        boolean saved = abstractUserService.saveUser(user);
        System.out.println("Save result: " + saved);
        
        // 等待一小段时间确保数据库操作完成
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 验证保存后的值 - 强制从数据库重新加载
        AbstractUser savedUser = abstractUserService.findUserById(user.getId());
        System.out.println("Reloaded user avatar: " + (savedUser != null ? savedUser.getAvatar() : "null"));
        System.out.println("=== Avatar Update End ===");
        return saved;
    }

    /**
     * 获取用户个人信息
     * @param userID 用户ID
     * @return AbstractUserDto
     */
    @GetMapping("/info/get")
    public AbstractUserDto getProfile(@RequestParam("userID") long userID){
        AbstractUser user = abstractUserService.findUserById(userID);
        if(user == null){
            throw new MyException(0, "User not found");
        }
        AbstractUserDto abstractUserDto = new AbstractUserDto();
        abstractUserDto.setId(user.getId());
        abstractUserDto.setName(user.getName());
        abstractUserDto.setBio(user.getBio());
        abstractUserDto.setAvatar(user.getAvatar());
        return abstractUserDto;
    }

    /**
     * 获取用户发布的帖子
     * @param token 用户token
     * @return List<PostDto>
     */
    @GetMapping("/post")
    @org.springframework.transaction.annotation.Transactional
    public List<PostDto> getProfilePost(@RequestHeader("Authorization") String token){
        AbstractUser user = JwtUtil.verifyToken(token);
        if(user == null){
            throw new MyException(0, "Invalid token");
        }
        List<PostDto> postDtoList = new ArrayList<>();
        List<Post> posts = postService.findPostsByUserId(user.getId());
        System.out.println("getProfilePost - User ID: " + user.getId() + ", Found " + posts.size() + " posts");
        for(Post post : posts){
            try {
                // 在事务内初始化懒加载属性
                if (post.getUser() != null) {
                    post.getUser().getId();
                    post.getUser().getName();
                }
                if (post.getEvent() != null) {
                    post.getEvent().getId(); // 触发懒加载
                }
                if (post.getLikeUsers() != null) {
                    post.getLikeUsers().size(); // 触发懒加载
                }
                PostDto postDto = new PostDto(post);
                postDto.setPostCollectAmount(post.getPostCollectAmount());
                postDto.setPostCommentAmount(eventCommentService.findEventCommentByPostId(post.getId()).size());
                System.out.println("getProfilePost - Created PostDto: ID=" + postDto.getPostID() + ", Title=" + postDto.getPostTitle());
                postDtoList.add(postDto);
            } catch (Exception e) {
                System.err.println("getProfilePost - Error creating PostDto for post ID " + post.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("getProfilePost - Returning " + postDtoList.size() + " PostDtos");
        return postDtoList;
    }
    
    /**
     * 获取用户发布的活动
     * @param token 用户token
     * @return List<EventBriefDto>
     */
    @GetMapping("/event")
    @org.springframework.transaction.annotation.Transactional
    public List<org.example.backend.dto.EventBriefDto> getProfileEvent(@RequestHeader("Authorization") String token){
        AbstractUser user = JwtUtil.verifyToken(token);
        if(user == null){
            throw new MyException(0, "Invalid token");
        }
        List<org.example.backend.domain.Event> events = eventService.findEventByAuthorId(user.getId());
        System.out.println("getProfileEvent - User ID: " + user.getId() + ", Found " + events.size() + " events");
        List<org.example.backend.dto.EventBriefDto> eventDtoList = new ArrayList<>();
        for(org.example.backend.domain.Event event : events){
            // 在事务内初始化所有懒加载属性
            if (event.getAuthor() != null) {
                event.getAuthor().getId();
                event.getAuthor().getName();
            }
            if (event.getAbstractEnrollment() != null) {
                event.getAbstractEnrollment().getStartTime();
                event.getAbstractEnrollment().getEndTime();
            }
            eventDtoList.add(new org.example.backend.dto.EventBriefDto(event));
        }
        return eventDtoList;
    }

    /**
     * 获取用户收藏的活动
     * @param token 用户token
     * @return List<EventBriefDto>
     */
    @GetMapping("/favorite/events")
    @org.springframework.transaction.annotation.Transactional
    public List<org.example.backend.dto.EventBriefDto> getFavoriteEvents(@RequestHeader("Authorization") String token) {
        AbstractUser user = JwtUtil.verifyToken(token);
        if (!(user instanceof User u)) {
            throw new MyException(-1, "Only user can have favorite events");
        }
        List<org.example.backend.dto.EventBriefDto> result = new ArrayList<>();
        if (u.getFavouriteEvents() != null) {
            // 触发懒加载
            u.getFavouriteEvents().size();
            for (org.example.backend.domain.Event ev : u.getFavouriteEvents()) {
                result.add(new org.example.backend.dto.EventBriefDto(ev));
            }
        }
        return result;
    }

    /**
     * 获取用户收藏的帖子
     * @param token 用户token
     * @return List<PostDto>
     */
    @GetMapping("/favorite/posts")
    @org.springframework.transaction.annotation.Transactional
    public List<PostDto> getFavoritePosts(@RequestHeader("Authorization") String token) {
        AbstractUser user = JwtUtil.verifyToken(token);
        if (!(user instanceof User u)) {
            throw new MyException(-1, "Only user can have favorite posts");
        }
        List<PostDto> result = new ArrayList<>();
        if (u.getFavouritePosts() != null) {
            // 触发懒加载
            u.getFavouritePosts().size();
            for (Post p : u.getFavouritePosts()) {
                try {
                    result.add(new PostDto(p));
                } catch (Exception e) {
                    System.err.println("getFavoritePosts - error converting post " + p.getId() + ": " + e.getMessage());
                }
            }
        }
        return result;
    }
}
