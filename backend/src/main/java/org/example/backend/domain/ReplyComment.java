package org.example.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
public class ReplyComment extends Comment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_comment_id")
    private Comment toComment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "under_comment_id")
    private EventComment underComment;


    public Comment getToComment() {
        return toComment;
    }

    public void setToComment(Comment toComment) {
        this.toComment = toComment;
    }

    public EventComment getUnderComment() {
        return underComment;
    }

    public void setUnderComment(EventComment underComment) {
        this.underComment = underComment;
    }
}
