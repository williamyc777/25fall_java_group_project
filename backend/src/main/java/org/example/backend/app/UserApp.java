package org.example.backend.app;

import org.example.backend.domain.*;
import org.example.backend.dto.UserDto;
import org.example.backend.service.*;
import org.example.backend.util.JwtUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 该类用于处理用户相关的请求
 * @author wangyr
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserApp {

    @Autowired
    private EventCommentService eventCommentService;
    @Autowired
    CommentService commentService;
    @Autowired
    private AbstractUserService abstractUserService;
    @Autowired
    private ReplyCommentService replyCommentService;
    @Autowired
    private EventService eventService;
    @Autowired
    private PostService postService;
    @Autowired
    private MessageService messageService;

    /**
     * 评论活动
     * @param token 用户token
     * @param eventId 活动id
     * @param comment 评论内容
     * @return boolean
     */
    @PostMapping(value = "/commentEvent")
    boolean commentEvent(@RequestHeader("Authorization") String token, @RequestParam long eventId, @RequestParam String comment) {
        long userId = JwtUtil.getIdByToken(token);
        User user = (User) abstractUserService.findUserById(userId);
        Event event = eventService.findEventById(eventId);
        EventComment eventComment = new EventComment();
        eventComment.setEvent(event);
        eventComment.setUser(user);
        eventComment.setComment(comment);
        eventCommentService.saveEventComment(eventComment);
        return true;
    }

    /**
     * 评论帖子
     * @param token 用户token
     * @param postId 帖子id
     * @param comment 评论内容
     * @return boolean
     */
    @PostMapping(value = "/commentPost")
    boolean commentPost(@RequestHeader("Authorization") String token, @RequestParam long postId, @RequestParam String comment) {
        long userId = JwtUtil.getIdByToken(token);
        User user = (User) abstractUserService.findUserById(userId);
        Post post = postService.findPostById(postId);
        EventComment eventComment = new EventComment();
        eventComment.setPost(post);
        eventComment.setUser(user);
        eventComment.setComment(comment);
        eventCommentService.saveEventComment(eventComment);
        return true;
    }

    /**
     * 回复评论
     * @param token 用户token
     * @param commentId 评论id
     * @param comment 回复内容
     * @return boolean
     */
    @PostMapping(value = "/reply")
    boolean replyComment(@RequestHeader("Authorization") String token, @RequestParam long commentId, @RequestParam String comment) {
        long userId = JwtUtil.getIdByToken(token);
        User user = (User) abstractUserService.findUserById(userId);
        EventComment tempComment = eventCommentService.findEventCommentById(commentId);
        ReplyComment replyComment = new ReplyComment();
        replyComment.setUser(user);
        replyComment.setComment(comment);
        if (tempComment == null) {
            ReplyComment toComment = replyCommentService.findReplyCommentById(commentId);
            if (toComment == null) {
                return false;
            }
            replyComment.setToComment(toComment);
            replyComment.setUnderComment(toComment.getUnderComment());
            replyCommentService.saveReplyComment(replyComment);
            toComment.getReplyComments().add(replyComment);
            // message
            JSONObject jsonObject = new JSONObject();
            EventComment eventComment = toComment.getUnderComment();
            if (eventComment.getEvent() == null) {
//                jsonObject.put("type", "post");
                jsonObject.put("postId", eventComment.getPost().getId());
                jsonObject.put("postTitle", eventComment.getPost().getPostTitle());
                jsonObject.put("commentContent", comment);
            } else {
//                jsonObject.put("type", "event");
                jsonObject.put("eventId", eventComment.getEvent().getId());
                jsonObject.put("eventTitle", eventComment.getEvent().getTitle());
                jsonObject.put("commentContent", comment);
            }
            jsonObject.put("oriComment", toComment.getComment());
            messageService.saveMessage(new Message("Comment", toComment.getUser(), user, false, LocalDateTime.now(), jsonObject.toString()));
            return commentService.updateComment(toComment);
        } else {
            replyComment.setUnderComment(tempComment);
            replyComment.setToComment(tempComment);
            replyCommentService.saveReplyComment(replyComment);
            tempComment.getReplyComments().add(replyComment);
            // message
            JSONObject jsonObject = new JSONObject();
            if (tempComment.getEvent() == null) {
//                jsonObject.put("type", "post");
                jsonObject.put("postId", tempComment.getPost().getId());
                jsonObject.put("postTitle", tempComment.getPost().getPostTitle());
                jsonObject.put("commentContent", comment);
            } else {
//                jsonObject.put("type", "event");
                jsonObject.put("eventId", tempComment.getEvent().getId());
                jsonObject.put("eventTitle", tempComment.getEvent().getTitle());
                jsonObject.put("commentContent", comment);
            }
            jsonObject.put("oriComment", tempComment.getComment());
            messageService.saveMessage(new Message("Comment", tempComment.getUser(), user, false, LocalDateTime.now(), jsonObject.toString()));
            return commentService.updateComment(tempComment);
        }
    }

    /**
     * 获取用户权限
     * @param token 用户token
     * @return UserDto
     */
    @GetMapping("/permission")
    public UserDto getUserPermission(@RequestHeader("Authorization") String token) {
        long userId = JwtUtil.getIdByToken(token);
        User user = (User) abstractUserService.findUserById(userId);
        UserDto userDto = new UserDto();
        userDto.setCanComment(user.getPermission().isCanComment());
        userDto.setCanCreate(user.getPermission().isCanCreate());
        userDto.setCanEnroll(user.getPermission().isCanEnroll());
        return userDto;
    }
}
