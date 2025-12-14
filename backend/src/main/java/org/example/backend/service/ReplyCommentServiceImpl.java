package org.example.backend.service;

import org.example.backend.api.ReplyCommentRepository;
import org.example.backend.domain.ReplyComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyCommentServiceImpl implements ReplyCommentService {

    @Autowired
    ReplyCommentRepository replyCommentRepository;

    @Override
    public ReplyComment findReplyCommentById(long id) {
        return replyCommentRepository.findById(id).orElse(null);
    }

    @Override
    public ReplyComment saveReplyComment(ReplyComment replyComment) {
        return replyCommentRepository.save(replyComment);
    }

    @Override
    public List<ReplyComment> findAllById(Long[] ids) {
        return replyCommentRepository.findAllById(List.of(ids));
    }

    @Override
    public List<ReplyComment> findAllByUserId(long id) {
        return null;
//        return replyCommentRepository.findAllByStudentId(id);
    }

    @Override
    public List<ReplyComment> findAllUnderId(long id) {
        return replyCommentRepository.findAllByUnderCommentId(id);
    }

    @Override
    public boolean deleteReplyComment(long commentId) {
        replyCommentRepository.deleteById(commentId);
        return true;
    }
}
