package org.example.backend.app;

import org.example.backend.config.MyException;
import org.example.backend.domain.*;
import org.example.backend.dto.EventDto;
import org.example.backend.dto.PostDto;
import org.example.backend.service.AbstractUserService;
import org.example.backend.service.EventCommentService;
import org.example.backend.service.EventService;
import org.example.backend.service.PostService;
import org.example.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类用于处理帖子相关的请求
 * @author Shinomiya
 * @version 1.0
 */
@RestController
@RequestMapping("/post")
public class PostApp {
    @Autowired
    private PostService postService;
    @Autowired
    private AbstractUserService abstractUserService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventCommentService eventCommentService;

    /**
     * 发布帖子
     * @param token 用户token
     * @param postTitle 帖子标题
     * @param postContent 帖子内容
     * @param eventId 相关事件ID
     * @return 帖子ID
     */
    @PostMapping("/releasePost")
    public Long releasePost(@RequestHeader("Authorization") String token, @RequestParam("postTitle") String postTitle, @RequestParam("postContent") String postContent, @RequestParam("postRelevantEvent") String eventId) {
        AbstractUser user = JwtUtil.verifyToken(token);
        if (user == null) {
            throw new MyException(0, "Invalid token");
        }
        
        System.out.println("releasePost - User ID: " + user.getId() + ", Username: " + user.getUsername());
        System.out.println("releasePost - Post Title: " + postTitle);
        System.out.println("releasePost - Event ID: " + eventId);
        
        Post post = new Post();
        post.setPostTitle(postTitle);
        post.setPostContent(postContent);
        post.setUser(user);
        // 处理eventId，如果为"null"或空字符串则不关联活动
        if (eventId != null && !eventId.trim().isEmpty() && !eventId.equals("null")) {
            try {
                Long eventIdLong = Long.parseLong(eventId);
                post.setEvent(eventService.findEventById(eventIdLong));
            } catch (NumberFormatException e) {
                // eventId格式错误，不关联活动
                post.setEvent(null);
            }
        } else {
            post.setEvent(null);
        }
        
        boolean saved = postService.savePost(post);
        System.out.println("releasePost - Post saved: " + saved + ", Post ID: " + post.getId());
        
        // 验证帖子是否已保存
        Post savedPost = postService.findPostById(post.getId());
        if (savedPost == null) {
            System.out.println("releasePost - ERROR: Post not found after save!");
        } else {
            System.out.println("releasePost - Verified post exists: " + savedPost.getId());
        }
        
        return post.getId();
    }

    /**
     * 获取帖子
     * @param postId 帖子ID
     * @param token 用户token（可选，用于检查当前用户的点赞/收藏状态）
     * @return 帖子信息
     */
    @GetMapping("/getFullPost")
    @org.springframework.transaction.annotation.Transactional
    public PostDto getFullPost(@RequestParam("postID") String postId, @RequestHeader(value = "Authorization", required = false) String token) {
        Post post = postService.findPostById(Long.parseLong(postId));
        if (post == null) {
            throw new MyException(0, "Post not found");
        }
        
        // 在事务中访问懒加载集合，确保Session仍然打开
        // 初始化懒加载集合
        if (post.getLikeUsers() != null) {
            post.getLikeUsers().size(); // 触发懒加载
        }
        if (post.getUser() != null) {
            post.getUser().getId(); // 确保user已加载
        }
        
        AbstractUser currentUser = null;
        if (token != null && !token.isEmpty()) {
            try {
                currentUser = JwtUtil.verifyToken(token);
                // 强制重新加载当前用户数据
                if (currentUser != null) {
                    currentUser = abstractUserService.findUserById(currentUser.getId());
                }
            } catch (Exception e) {
                // Token无效，忽略
                System.out.println("Token verification failed: " + e.getMessage());
            }
        }
        
        System.out.println("getFullPost - postId: " + postId);
        System.out.println("getFullPost - currentUser: " + (currentUser != null ? currentUser.getId() : "null"));
        System.out.println("getFullPost - post author: " + post.getUser().getId() + ", name: " + post.getUser().getName());
        
        PostDto postDto = constructPostDto(post, currentUser);
        
        System.out.println("getFullPost - postDto username: " + postDto.getUsername());
        System.out.println("getFullPost - postDto likeOrNot: " + postDto.isLikeOrNot());
        System.out.println("getFullPost - postDto collectOrNot: " + postDto.isCollectOrNot());
        
        return postDto;
    }

    private PostDto constructPostDto(Post post, AbstractUser currentUser) {
        // 强制从数据库重新加载用户数据，确保获取最新的name等信息
        AbstractUser postUser = post.getUser();
        if (postUser == null) {
            System.out.println("constructPostDto - ERROR: post.getUser() is null!");
            throw new MyException(0, "Post author not found");
        }
        
        AbstractUser user = abstractUserService.findUserById(postUser.getId());
        if (user == null) {
            System.out.println("constructPostDto - WARNING: Failed to reload user, using cached user");
            user = postUser; // 如果重新加载失败，使用原来的user
        }
        
        System.out.println("constructPostDto - user ID: " + user.getId());
        System.out.println("constructPostDto - user name: " + user.getName());
        System.out.println("constructPostDto - user username: " + user.getUsername());
        
        PostDto postDto = new PostDto();
        postDto.setPostID(post.getId());
        postDto.setPostTitle(post.getPostTitle());
        postDto.setPostContent(post.getPostContent());
        postDto.setPostLink(String.valueOf(post.getId()));
        // 处理event可能为null的情况
        if (post.getEvent() != null) {
            postDto.setPostRelevantEventID(post.getEvent().getId());
        }
        // 获取点赞用户列表（已在事务中，可以安全访问）
        List<AbstractUser> likeUsers = post.getLikeUsers();
        postDto.setPostLikeAmount(likeUsers != null ? likeUsers.size() : 0);
        postDto.setPostCollectAmount(post.getPostCollectAmount());
        postDto.setPostCommentAmount(eventCommentService.findEventCommentByPostId(post.getId()).size());
        // 使用用户的名字（name）而不是用户名（username）来显示
        String displayName = user.getName() != null && !user.getName().isEmpty() ? user.getName() : user.getUsername();
        postDto.setUsername(displayName);
        System.out.println("constructPostDto - setting username to: " + displayName);
        postDto.setUserID(user.getId());
        postDto.setUserBio(user.getBio());
        postDto.setUserAvatar(user.getAvatar());
        
        // 检查当前登录用户是否点赞了该帖子（使用ID比较，避免对象比较问题）
        if (currentUser != null) {
            boolean liked = false;
            if (likeUsers != null && !likeUsers.isEmpty()) {
                liked = likeUsers.stream()
                    .anyMatch(u -> u != null && u.getId() == currentUser.getId());
            }
            postDto.setLikeOrNot(liked);
            System.out.println("constructPostDto - currentUser ID: " + currentUser.getId() + ", liked: " + liked);
            
            // 只有当currentUser是User类型时才检查收藏
            if (currentUser instanceof User) {
                // 强制重新加载用户数据以获取最新的收藏列表
                AbstractUser freshCurrentUser = abstractUserService.findUserById(currentUser.getId());
                if (freshCurrentUser instanceof User) {
                    boolean collected = false;
                    if (((User) freshCurrentUser).getFavouritePosts() != null) {
                        collected = ((User) freshCurrentUser).getFavouritePosts().stream()
                            .anyMatch(p -> p != null && p.getId() == post.getId());
                    }
                    postDto.setCollectOrNot(collected);
                    System.out.println("constructPostDto - collected: " + collected);
                } else {
                    postDto.setCollectOrNot(false);
                }
            } else {
                postDto.setCollectOrNot(false);
            }
        } else {
            // 如果没有当前用户，默认未点赞和未收藏
            postDto.setLikeOrNot(false);
            postDto.setCollectOrNot(false);
            System.out.println("constructPostDto - no currentUser, setting likeOrNot and collectOrNot to false");
        }
        return postDto;
    }
    
    // 重载方法，用于不需要当前用户信息的场景
    private PostDto constructPostDto(Post post) {
        return constructPostDto(post, null);
    }

    /**
     * @param token 用户token
     * @param postId 帖子ID
     * @return 点赞帖子是否成功
     */
    @PostMapping("/likeThePost")
    public boolean likeThePost(@RequestHeader("Authorization") String token, @RequestParam("postID") String postId) {
        Post post = postService.findPostById(Long.parseLong(postId));
        if (post == null) {
            throw new MyException(0, "Post not found");
        }
        AbstractUser user = JwtUtil.verifyToken(token);
        if (user == null) {
            throw new MyException(0, "Invalid token");
        }
        // 强制重新加载用户数据
        final long userId = user.getId();
        user = abstractUserService.findUserById(userId);
        if (user == null) {
            throw new MyException(0, "User not found");
        }
        
        List<AbstractUser> likeUsers = post.getLikeUsers();
        // 使用ID比较，避免对象比较问题
        boolean alreadyLiked = likeUsers != null && likeUsers.stream()
            .anyMatch(u -> u != null && u.getId() == userId);
        if (alreadyLiked) {
            System.out.println("User " + userId + " already liked post " + postId);
            return true;
        }
        likeUsers.add(user);
        boolean saved = postService.savePost(post);
        System.out.println("User " + userId + " liked post " + postId + ", saved: " + saved);
        return saved;
    }

    /**
     * @param token 用户token
     * @param postId 帖子ID
     * @return 取消点赞帖子是否成功
     */
    @PostMapping("/dislikeThePost")
    public boolean dislikeThePost(@RequestHeader("Authorization") String token, @RequestParam("postID") String postId) {
        Post post = postService.findPostById(Long.parseLong(postId));
        if (post == null) {
            throw new MyException(0, "Post not found");
        }
        AbstractUser user = JwtUtil.verifyToken(token);
        if (user == null) {
            throw new MyException(0, "Invalid token");
        }
        List<AbstractUser> likeUsers = post.getLikeUsers();
        // 使用ID比较，避免对象比较问题
        AbstractUser toRemove = likeUsers.stream()
            .filter(u -> u.getId() == user.getId())
            .findFirst()
            .orElse(null);
        if (toRemove == null) {
            return false;
        }
        likeUsers.remove(toRemove);
        return postService.savePost(post);
    }

    /**
     * @param token 用户token
     * @param postId 帖子ID
     * @return 收藏帖子是否成功
     */
    @PostMapping("/collectThePost")
    @org.springframework.transaction.annotation.Transactional
    public boolean collectThePost(@RequestHeader("Authorization") String token, @RequestParam("postID") String postId) {
        Post post = postService.findPostById(Long.parseLong(postId));
        if (post == null) {
            throw new MyException(0, "Post not found");
        }
        AbstractUser user = JwtUtil.verifyToken(token);
        if (user == null) {
            throw new MyException(0, "Invalid token");
        }
        if (!(user instanceof User)) {
            throw new MyException(0, "Only user can collect post");
        }
        // 强制重新加载用户数据，确保获取最新的收藏列表
        AbstractUser freshUser = abstractUserService.findUserById(user.getId());
        if (!(freshUser instanceof User)) {
            throw new MyException(0, "User not found");
        }
        User userObj = (User) freshUser;
        // 确保收藏列表已初始化（触发懒加载）
        if (userObj.getFavouritePosts() == null) {
            // 如果为null，可能需要初始化
            System.out.println("Warning: favouritePosts is null for user " + userObj.getId());
        } else {
            userObj.getFavouritePosts().size(); // 触发懒加载
        }
        
        // 使用ID比较，避免对象比较问题
        boolean alreadyCollected = userObj.getFavouritePosts() != null && 
            userObj.getFavouritePosts().stream()
                .anyMatch(p -> p != null && p.getId() == post.getId());
        if (alreadyCollected) {
            System.out.println("User " + userObj.getId() + " already collected post " + postId);
            return true;
        }
        userObj.getFavouritePosts().add(post);
        int oldAmount = post.getPostCollectAmount();
        post.setPostCollectAmount(oldAmount + 1);
        System.out.println("Collect post - old amount: " + oldAmount + ", new amount: " + post.getPostCollectAmount());
        
        // 同时保存用户和帖子，确保收藏数更新
        boolean userSaved = abstractUserService.saveUser(userObj);
        boolean postSaved = postService.savePost(post);
        System.out.println("Collect post - user saved: " + userSaved + ", post saved: " + postSaved);
        return userSaved && postSaved;
    }

    /**
     * @param token 用户token
     * @param postId 帖子ID
     * @return 取消收藏帖子是否成功
     */
    @PostMapping("/discollectThePost")
    @org.springframework.transaction.annotation.Transactional
    public boolean discollectThePost(@RequestHeader("Authorization") String token, @RequestParam("postID") String postId) {
        Post post = postService.findPostById(Long.parseLong(postId));
        if (post == null) {
            throw new MyException(0, "Post not found");
        }
        AbstractUser user = JwtUtil.verifyToken(token);
        if (user == null) {
            throw new MyException(0, "Invalid token");
        }
        if (!(user instanceof User)) {
            throw new MyException(1, "Only user can discollect post");
        }
        // 强制重新加载用户数据，确保获取最新的收藏列表
        AbstractUser freshUser = abstractUserService.findUserById(user.getId());
        if (!(freshUser instanceof User)) {
            throw new MyException(0, "User not found");
        }
        User userObj = (User) freshUser;
        // 使用ID比较，避免对象比较问题
        Post toRemove = userObj.getFavouritePosts() != null ? 
            userObj.getFavouritePosts().stream()
                .filter(p -> p != null && p.getId() == post.getId())
                .findFirst()
                .orElse(null) : null;
        if (toRemove == null) {
            System.out.println("User " + userObj.getId() + " did not collect post " + postId);
            return false;
        }
        userObj.getFavouritePosts().remove(toRemove);
        int oldAmount = post.getPostCollectAmount();
        post.setPostCollectAmount(Math.max(0, oldAmount - 1)); // 确保不会变成负数
        System.out.println("Discollect post - old amount: " + oldAmount + ", new amount: " + post.getPostCollectAmount());
        
        // 同时保存用户和帖子，确保收藏数更新
        boolean userSaved = abstractUserService.saveUser(userObj);
        boolean postSaved = postService.savePost(post);
        System.out.println("Discollect post - user saved: " + userSaved + ", post saved: " + postSaved);
        return userSaved && postSaved;
    }

    /**
     * 帖子广场列表
     * @param eventId 可选的活动ID，如果提供则只返回与该活动关联的帖子
     * @return 帖子列表
     */
    @GetMapping("/getPostSquare")
    public List<PostDto> getPostSquare(@RequestParam(value = "eventId", required = false) Long eventId) {
        List<Post> posts = postService.findAllPosts();
        System.out.println("getPostSquare - Found " + posts.size() + " posts (before filter)");

        // 如果指定了 eventId，则只保留关联到该活动的帖子
        if (eventId != null) {
            posts = posts.stream()
                    .filter(p -> p != null && p.getEvent() != null && p.getEvent().getId() == eventId)
                    .toList();
            System.out.println("getPostSquare - Filtered by eventId=" + eventId + ", remaining posts: " + posts.size());
        }

        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            PostDto dto = constructPostDto(post);
            System.out.println("getPostSquare - Post ID: " + dto.getPostID() + ", Title: " + dto.getPostTitle() + ", Username: " + dto.getUsername());
            postDtos.add(dto);
        }
        System.out.println("getPostSquare - Returning " + postDtos.size() + " post DTOs");
        return postDtos;
    }

    /**
     * @return 所有事件
     */
    @GetMapping("/getAllEvent")
    public List<EventDto> getAllEvent() {
        List<EventDto> eventDtos = new ArrayList<>();
        List<Event> events = eventService.findAllEvents();
        for (Event event : events) {
            eventDtos.add(constructEventDto(event));
        }
        return eventDtos;
    }

    private EventDto constructEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setEventName(event.getName());
        return eventDto;
    }

    /**
     * @param postId 帖子ID
     * @return 删除帖子是否成功
     */
    @DeleteMapping("/delete/{postID}")
    public boolean deletePost(@PathVariable("postID") String postId) {
        return postService.deletePostById(Long.parseLong(postId));
    }

    /**
     * @param token 用户token
     * @return 用户收藏的帖子
     */
    @GetMapping("/getPostSquare/collect")
    public List<PostDto> getPostSquareCollect(@RequestHeader("Authorization") String token) {
        AbstractUser user = JwtUtil.verifyToken(token);
        List<Post> posts = ((User) user).getFavouritePosts();
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(constructPostDto(post));
        }
        return postDtos;
    }

    /**
     * @param token 用户token
     * @return 用户发布的帖子
     */
    @GetMapping("/getPostSquare/write")
    public List<PostDto> getPostSquareWrite(@RequestHeader("Authorization") String token) {
        AbstractUser user = JwtUtil.verifyToken(token);
        List<Post> posts = postService.findPostsByUserId(user.getId());
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(constructPostDto(post));
        }
        return postDtos;
    }

}
