package org.example.backend.service;

import org.example.backend.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAll();

    Comment findCommentById(long id);

    Boolean updateComment(Comment comment);
}
