package org.example.backend.app;

import org.example.backend.domain.EventComment;
import org.example.backend.domain.ReplyComment;
import org.example.backend.dto.EventCommentDto;
import org.example.backend.dto.ReplyCommentDto;
import org.example.backend.service.EventCommentService;
import org.example.backend.service.ReplyCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类用于处理评论相关的请求
 * @author wangyr
 * @version 1.0
 */
@RestController
@RequestMapping("/comment")
public class CommentApp {
    private final EventCommentService eventCommentService;
    private final ReplyCommentService replyCommentService;

    @Autowired
    public CommentApp(EventCommentService eventCommentService, ReplyCommentService replyCommentService) {
        this.eventCommentService = eventCommentService;
        this.replyCommentService = replyCommentService;
    }

    /**
     * @param postId 帖子ID
     * @return 评论列表
     */
    @GetMapping(value = "/event", params = {"postId"})
    @Transactional
    public List<EventCommentDto> getEventCommentByPostId(long postId) {
        List<EventCommentDto> eventCommentDtos = new ArrayList<>();
        for (EventComment eventComment : eventCommentService.findEventCommentByPostId(postId)) {
            // 强制初始化懒加载的用户对象
            if (eventComment.getUser() != null) {
                String userName = eventComment.getUser().getName();
                String userAvatar = eventComment.getUser().getAvatar();
                System.out.println("Comment ID: " + eventComment.getId() + ", User: " + userName + ", Avatar: " + userAvatar);
            }
            EventCommentDto dto = new EventCommentDto(eventComment);
            System.out.println("DTO - userName: " + dto.getUserName() + ", userAvatar: " + dto.getUserAvatar());
            eventCommentDtos.add(dto);
        }
        return eventCommentDtos;
    }

    /**
     * @param eventId 活动ID
     * @return 评论列表
     */
    @GetMapping(value = "/event", params = {"eventId"})
    public List<EventCommentDto> getEventCommentByEventId(long eventId) {
        List<EventCommentDto> eventCommentDtos = new ArrayList<>();
        for (EventComment eventComment : eventCommentService.findEventCommentByEventId(eventId)) {
            eventCommentDtos.add(new EventCommentDto(eventComment));
        }
        return eventCommentDtos;
    }

    /**
     * @param commentId 评论ID
     * @return 对应评论
     */
    @GetMapping(value = "/event", params = {"commentId"})
    public EventCommentDto getEventCommentByID(long commentId) {
        EventComment eventComment = eventCommentService.findEventCommentById(commentId);
        return new EventCommentDto(eventComment);
    }

    /**
     * @param commentIds 评论ID数组
     * @return 对应评论列表
     */
    @GetMapping(value = "/event", params = {"commentIds"})
    public List<EventCommentDto> getAllEventCommentById(Long[] commentIds) {
        List<EventCommentDto> eventCommentDtos = new ArrayList<>();
        List<EventComment> eventComments = eventCommentService.findAllById(commentIds);
        for (EventComment eventComment : eventComments) {
            eventCommentDtos.add(new EventCommentDto(eventComment));
        }
        return eventCommentDtos;
    }

    /**
     * @param commentId 评论ID
     * @return 对应评论的回复评论
     */
    @GetMapping(value = "/reply", params = {"commentId"})
    public ReplyCommentDto getReplyCommentByID(Long commentId) {
        ReplyComment replyComment = replyCommentService.findReplyCommentById(commentId);
        return new ReplyCommentDto(replyComment);
    }

    /**
     * @param commentIds 评论ID数组
     * @return 回复评论列表
     */
    @GetMapping(value = "/reply", params = {"commentIds"})
    public List<ReplyCommentDto> getAllReplyCommentById(Long[] commentIds) {
        List<ReplyCommentDto> replyCommentDtos = new ArrayList<>();
        List<ReplyComment> replyComments = replyCommentService.findAllById(commentIds);
        for (ReplyComment replyComment : replyComments) {
            replyCommentDtos.add(new ReplyCommentDto(replyComment));
        }
        return replyCommentDtos;
    }

    /**
     * @param commentId 评论ID
     * @return 对应评论的回复评论列表
     */
    @GetMapping(value = "/under", params = {"commentId"})
    public List<ReplyCommentDto> getAllReplyCommentUnderId(Long commentId) {
        List<ReplyCommentDto> replyCommentDtos = new ArrayList<>();
        List<ReplyComment> replyComments = replyCommentService.findAllUnderId(commentId);
        for (ReplyComment replyComment : replyComments) {
            replyCommentDtos.add(new ReplyCommentDto(replyComment));
        }
        return replyCommentDtos;
    }
}
