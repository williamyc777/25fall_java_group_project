package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.domain.EventComment;
import org.example.backend.domain.ReplyComment;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDto {
    private long id;
    private String comment;
    private long userId;
    private String userName;
    private long postId;
    private List<Long> replyCommentIds;

    public PostCommentDto(EventComment eventComment) {
        this.id = eventComment.getId();
        this.comment = eventComment.getComment();
        this.userId = eventComment.getUser().getId();
        this.postId = eventComment.getPost().getId();
        this.userName = eventComment.getUser().getName();
        this.replyCommentIds = eventComment.getReplyComments().stream().map(ReplyComment::getId).toList();
    }
}
