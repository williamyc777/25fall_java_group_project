package org.example.backend.service;

import org.example.backend.api.CommentRepository;
import org.example.backend.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }


    @Override
    public Boolean updateComment(Comment comment) {
        commentRepository.save(comment);
        return true;
    }
}
