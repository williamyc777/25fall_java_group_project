package org.example.backend.service;

import org.example.backend.domain.ReplyComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReplyCommentService {
    ReplyComment findReplyCommentById(long id);

    ReplyComment saveReplyComment(ReplyComment replyComment);
    List<ReplyComment> findAllById(Long[] ids);
    List<ReplyComment> findAllByUserId(long id);
    List<ReplyComment> findAllUnderId(long id);

    boolean deleteReplyComment(long commentId);
}
