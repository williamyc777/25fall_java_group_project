package org.example.backend.service;

import jakarta.persistence.EntityManager;
import org.example.backend.api.EventCommentRepository;
import org.example.backend.domain.EventComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventCommentServiceImpl implements EventCommentService {
    @Autowired
    EventCommentRepository eventCommentRepository;
    
    @Autowired
    private EntityManager entityManager;

    @Override
    public EventComment findEventCommentById(long id) {
        return eventCommentRepository.findById(id).orElse(null);
    }

    @Override
    public List<EventComment> findEventCommentByEventId(long id) {
        return eventCommentRepository.findEventCommentByEventId(id);
    }

    @Override
    public EventComment saveEventComment(EventComment eventComment) {
        return eventCommentRepository.save(eventComment);
    }

    @Override
    public List<EventComment> findAllById(Long[] ids) {
        return eventCommentRepository.findAllById(List.of(ids));
    }

    @Override
    public List<EventComment> findAllByUserId(long id) {
        return eventCommentRepository.findAllByUserId(id);
    }

    @Override
    @Transactional
    public List<EventComment> findEventCommentByPostId(long postId) {
        // 使用JPQL查询，使用JOIN FETCH立即加载懒加载的用户对象
        entityManager.clear();
        try {
            List<EventComment> comments = entityManager.createQuery(
                "SELECT ec FROM EventComment ec " +
                "LEFT JOIN FETCH ec.user " +
                "LEFT JOIN FETCH ec.replyComments " +
                "WHERE ec.post.id = :postId", EventComment.class)
                .setParameter("postId", postId)
                .getResultList();
            return comments;
        } catch (Exception e) {
            // 如果JPQL查询失败，回退到Repository方法
            return eventCommentRepository.findEventCommentByPostId(postId);
        }
    }

    @Override
    public boolean deleteEventComment(long commentId) {
        eventCommentRepository.deleteById(commentId);
        return true;
    }
}
