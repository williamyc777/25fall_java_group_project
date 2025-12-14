package org.example.backend.api;

import org.example.backend.domain.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {
    List<ReplyComment> findReplyCommentByToCommentId(long id);

    List<ReplyComment> findAllByUserId(long id);

    List<ReplyComment> findAllByUnderCommentId(long id);
}
