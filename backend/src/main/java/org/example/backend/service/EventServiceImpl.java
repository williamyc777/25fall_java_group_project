package org.example.backend.service;

import jakarta.persistence.EntityManager;
import org.example.backend.api.AbstractEnrollmentRepository;
import org.example.backend.api.EnrollFormRepository;
import org.example.backend.api.EventRepository;
import org.example.backend.api.ScoreRepository;
import org.example.backend.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EnrollFormRepository enrollFormRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    AbstractEnrollmentRepository abstractEnrollmentRepository;
    
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean saveEvent(Event event) {
        eventRepository.save(event);
        // 立即刷新到数据库，确保数据已持久化
        entityManager.flush();
        // 清除EntityManager缓存，确保下次查询时获取最新数据
        entityManager.clear();
        System.out.println("EventServiceImpl.saveEvent - Event saved with ID: " + event.getId() + ", Author ID: " + (event.getAuthor() != null ? event.getAuthor().getId() : "null"));
        return true;
    }

    @Override
    public boolean deleteEventById(long id) {
        eventRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateEvent(Event event) {
        eventRepository.save(event);
        return true;
    }

    @Override
    @Transactional
    public Event findEventById(long id) {
        // 清除EntityManager缓存，确保获取最新数据
        entityManager.clear();
        // 使用JPQL JOIN FETCH 来预加载所有需要的关联
        Event event = entityManager.createQuery(
            "SELECT e FROM Event e " +
            "LEFT JOIN FETCH e.author " +
            "LEFT JOIN FETCH e.abstractEnrollment " +
            "LEFT JOIN FETCH e.posts " +
            "WHERE e.id = :id", Event.class)
            .setParameter("id", id)
            .getResultStream()
            .findFirst()
            .orElse(null);
        
        if (event != null) {
            // 强制初始化懒加载的集合
            if (event.getAbstractEnrollment() != null) {
                AbstractEnrollment enrollment = event.getAbstractEnrollment();
                if (enrollment.getParticipants() != null) {
                    enrollment.getParticipants().size(); // 触发懒加载
                }
                // 如果是 FormEnrollment，初始化 definedFormEntries
                if (enrollment instanceof org.example.backend.domain.FormEnrollment formEnrollment) {
                    if (formEnrollment.getDefinedFormEntries() != null) {
                        formEnrollment.getDefinedFormEntries().size(); // 触发懒加载
                    }
                }
            }
        }
        return event;
    }

    @Override
    @Transactional
    public List<Event> findAllEvents() {
        // 清除EntityManager缓存，确保获取最新数据
        entityManager.clear();
        // 使用JPQL查询并指定排序，确保每次返回的顺序一致
        return entityManager.createQuery(
            "SELECT e FROM Event e ORDER BY e.id DESC", Event.class)
            .getResultList();
    }

    @Override
    @Transactional
    public List<Event> findEventByAuthorId(long authorId) {
        // 清除EntityManager缓存，确保获取最新数据
        entityManager.clear();
        
        System.out.println("EventServiceImpl.findEventByAuthorId - Starting query for author ID: " + authorId);
        
        // 首先尝试使用JPQL查询（更可靠）
        List<Event> events;
        try {
            events = entityManager.createQuery(
                "SELECT e FROM Event e WHERE e.author.id = :authorId", Event.class)
                .setParameter("authorId", authorId)
                .getResultList();
            System.out.println("EventServiceImpl.findEventByAuthorId - JPQL query found " + events.size() + " events for author ID: " + authorId);
        } catch (Exception e) {
            System.err.println("EventServiceImpl.findEventByAuthorId - JPQL query failed: " + e.getMessage());
            e.printStackTrace();
            // 如果JPQL失败，尝试Repository方法
            events = eventRepository.findByAuthor_Id(authorId);
            System.out.println("EventServiceImpl.findEventByAuthorId - Repository fallback found " + events.size() + " events for author ID: " + authorId);
        }
        
        // 验证查询结果并记录详细信息
        if (events.isEmpty()) {
            System.out.println("EventServiceImpl.findEventByAuthorId - No events found for author ID: " + authorId);
            // 尝试查询所有活动，看看数据库中是否有活动
            List<Event> allEvents = eventRepository.findAll();
            System.out.println("EventServiceImpl.findEventByAuthorId - Total events in database: " + allEvents.size());
            allEvents.forEach(event -> {
                if (event.getAuthor() != null) {
                    System.out.println("EventServiceImpl.findEventByAuthorId - Event ID: " + event.getId() + ", Author ID: " + event.getAuthor().getId() + ", Author class: " + event.getAuthor().getClass().getSimpleName());
                } else {
                    System.out.println("EventServiceImpl.findEventByAuthorId - Event ID: " + event.getId() + ", Author is NULL");
                }
            });
        } else {
            events.forEach(event -> {
                System.out.println("EventServiceImpl.findEventByAuthorId - Found Event ID: " + event.getId() + ", Author ID: " + (event.getAuthor() != null ? event.getAuthor().getId() : "null"));
            });
        }
        
        return events;
    }

    @Override
    public boolean saveEnrollForm(EnrollForm enrollForm) {
        enrollFormRepository.save(enrollForm);
        return true;
    }

    @Override
    public long getScore(long userId, long eventId) {
        // 允许存在多条记录，这里取最新的一条（id 最大）
        List<Score> scores = scoreRepository.findAllByUserIdAndEventId(userId, eventId);
        if (scores == null || scores.isEmpty()) {
            return 0;
        }
        Score latest = scores.stream()
                .max(java.util.Comparator.comparingLong(Score::getId))
                .orElse(scores.get(0));
        return latest.getScore();
    }

    @Override
    public boolean saveScore(long userId, long eventId, long score) {
        // 如果已存在记录，则更新最新那条；否则新建
        List<Score> scores = scoreRepository.findAllByUserIdAndEventId(userId, eventId);
        Score scoreObj;
        if (scores == null || scores.isEmpty()) {
            scoreObj = new Score();
            scoreObj.setUserId(userId);
            scoreObj.setEventId(eventId);
        } else {
            scoreObj = scores.stream()
                    .max(java.util.Comparator.comparingLong(Score::getId))
                    .orElse(scores.get(0));
        }
        scoreObj.setScore(score);
        scoreRepository.save(scoreObj);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteEvent(long eventId) {
        // 先检查事件是否存在
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            System.out.println("deleteEvent - Event not found: " + eventId);
            return false;
        }
        
        // 1. 先删除所有用户收藏该活动的关系
        try {
            entityManager.createQuery(
                "DELETE FROM User u WHERE EXISTS (" +
                "  SELECT 1 FROM u.favouriteEvents fe WHERE fe.id = :eventId" +
                ")")
                .setParameter("eventId", eventId)
                .executeUpdate();
            
            // 使用JPQL删除多对多关系表中的记录
            entityManager.createQuery(
                "UPDATE User u SET u.favouriteEvents = NULL WHERE EXISTS (" +
                "  SELECT 1 FROM u.favouriteEvents fe WHERE fe.id = :eventId" +
                ")")
                .setParameter("eventId", eventId)
                .executeUpdate();
            
            // 更直接的方法：从所有用户的收藏列表中移除该活动
            List<User> usersWithFavourite = entityManager.createQuery(
                "SELECT DISTINCT u FROM User u JOIN u.favouriteEvents fe WHERE fe.id = :eventId", User.class)
                .setParameter("eventId", eventId)
                .getResultList();
            
            for (User user : usersWithFavourite) {
                if (user.getFavouriteEvents() != null) {
                    user.getFavouriteEvents().removeIf(e -> e != null && e.getId() == eventId);
                }
            }
            
            System.out.println("deleteEvent - Removed favourite relationships for " + usersWithFavourite.size() + " users");
        } catch (Exception e) {
            System.out.println("deleteEvent - Error removing favourite relationships: " + e.getMessage());
            // 继续删除，即使清理收藏关系失败
        }
        
        // 2. 删除评分关系（如果有）
        try {
            entityManager.createQuery(
                "UPDATE User u SET u.scoredEvents = NULL WHERE EXISTS (" +
                "  SELECT 1 FROM u.scoredEvents se WHERE se.id = :eventId" +
                ")")
                .setParameter("eventId", eventId)
                .executeUpdate();
            
            List<User> usersWithScored = entityManager.createQuery(
                "SELECT DISTINCT u FROM User u JOIN u.scoredEvents se WHERE se.id = :eventId", User.class)
                .setParameter("eventId", eventId)
                .getResultList();
            
            for (User user : usersWithScored) {
                if (user.getScoredEvents() != null) {
                    user.getScoredEvents().removeIf(e -> e != null && e.getId() == eventId);
                }
            }
            
            System.out.println("deleteEvent - Removed scored relationships for " + usersWithScored.size() + " users");
        } catch (Exception e) {
            System.out.println("deleteEvent - Error removing scored relationships: " + e.getMessage());
        }
        
        // 2.5. 删除 Score 表中的所有评分记录（必须先删除，因为有外键约束）
        try {
            List<Score> scoresToDelete = entityManager.createQuery(
                "SELECT s FROM Score s WHERE s.eventId = :eventId", Score.class)
                .setParameter("eventId", eventId)
                .getResultList();
            
            if (!scoresToDelete.isEmpty()) {
                scoreRepository.deleteAll(scoresToDelete);
                System.out.println("deleteEvent - Deleted " + scoresToDelete.size() + " score records for event " + eventId);
            }
        } catch (Exception e) {
            System.out.println("deleteEvent - Error deleting score records: " + e.getMessage());
            // 继续删除，即使清理评分记录失败
        }
        
        // 3. 刷新以确保关联关系已删除
        entityManager.flush();
        entityManager.clear();
        
        // 4. 删除事件本身（级联删除会自动处理 posts 和 comments）
        eventRepository.deleteById(eventId);
        
        // 5. 立即刷新到数据库，确保删除操作已提交
        entityManager.flush();
        entityManager.clear();
        
        System.out.println("deleteEvent - Event deleted successfully: " + eventId);
        return true;
    }

    @Override
    @Transactional
    public boolean appliedByUser(long userId, long eventId) {
        // 清除缓存，确保获取最新数据
        entityManager.clear();
        // 使用JPQL查询检查用户是否在participants列表中
        Long count = entityManager.createQuery(
            "SELECT COUNT(p) FROM AbstractEnrollment ae " +
            "JOIN ae.participants p " +
            "JOIN ae.event e " +
            "WHERE e.id = :eventId AND p.id = :userId", Long.class)
            .setParameter("eventId", eventId)
            .setParameter("userId", userId)
            .getSingleResult();
        
        // 如果是FormEnrollment，也检查EnrollForm
        if (count == 0) {
            count = entityManager.createQuery(
                "SELECT COUNT(ef) FROM EnrollForm ef " +
                "JOIN ef.formEnrollment fe " +
                "JOIN fe.event e " +
                "WHERE e.id = :eventId AND ef.user.id = :userId", Long.class)
                .setParameter("eventId", eventId)
                .setParameter("userId", userId)
                .getSingleResult();
        }
        
        return count > 0;
    }

    @Override
    public List<Event> searchEvent(String keyword) {
        return eventRepository.findAllByTextContainingIgnoreCaseOrIntroductionContainingIgnoreCaseOrNameContainingIgnoreCaseOrTitleContainingIgnoreCase(keyword, keyword, keyword, keyword);
    }
}