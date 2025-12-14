package org.example.backend.dto;

import org.example.backend.domain.ReplyComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyCommentDto {
    private long id;
    private String comment;
    private long userId;
    private String userName;
    private long toCommentId;
    private String toUserName;
    private long underCommentId;
    public ReplyCommentDto(ReplyComment replyComment) {
        this.id = replyComment.getId();
        this.comment = replyComment.getComment();
        this.userId = replyComment.getUser().getId();
        this.toCommentId = replyComment.getToComment().getId();
        this.underCommentId = replyComment.getUnderComment().getId();
        this.userName = replyComment.getUser().getName();
        this.toUserName = replyComment.getToComment().getUser().getName();
    }
}
