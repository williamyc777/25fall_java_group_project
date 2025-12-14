package org.example.backend.service;


import org.example.backend.domain.EventComment;

import java.util.List;

public interface EventCommentService {
    EventComment findEventCommentById(long id);

    List<EventComment> findEventCommentByEventId(long id);

    EventComment saveEventComment(EventComment eventComment);

    List<EventComment> findAllById(Long[] ids);

    List<EventComment> findAllByUserId(long id);

    List<EventComment> findEventCommentByPostId(long postId);

    boolean deleteEventComment(long commentId);

}
