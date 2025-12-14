package org.example.backend.api;

import org.example.backend.domain.EventComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventCommentRepository extends JpaRepository<EventComment, Long> {
    List<EventComment> findEventCommentByEventId(long id);

    List<EventComment> findAllByUserId(long id);

    List<EventComment> findEventCommentByPostId(long postId);
}
