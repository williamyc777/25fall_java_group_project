package org.example.backend.dto;

import org.example.backend.domain.EventComment;
import org.example.backend.domain.ReplyComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCommentDto {
    private long id;
    private String comment;
    private long userId;
    private String userName;
    private String userAvatar;
    private long eventId;
    private long postId;
    private List<Long> replyCommentIds;

    public EventCommentDto(EventComment eventComment) {
        this.id = eventComment.getId();
        this.comment = eventComment.getComment();
        if (eventComment.getUser() != null) {
            this.userId = eventComment.getUser().getId();
            this.userName = eventComment.getUser().getName();
            this.userAvatar = eventComment.getUser().getAvatar();
        } else {
            this.userId = 0;
            this.userName = "未知用户";
            this.userAvatar = null;
        }
        if (eventComment.getEvent() != null) {
            this.eventId = eventComment.getEvent().getId();
        }
        if (eventComment.getPost() != null) {
            this.postId = eventComment.getPost().getId();
        }
        if (eventComment.getReplyComments() != null) {
            this.replyCommentIds = eventComment.getReplyComments().stream().map(ReplyComment::getId).toList();
        } else {
            this.replyCommentIds = java.util.Collections.emptyList();
        }
    }
}
