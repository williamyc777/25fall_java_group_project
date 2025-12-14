package org.example.backend.api;

import org.example.backend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    // 通过author的ID查询活动 - 使用多种方法名尝试
    List<Event> findByAuthor_Id(long authorId);
    List<Event> findByAuthorId(long authorId);
    
    // 保持向后兼容
    default List<Event> findEventByAuthorId(long authorId) {
        // 先尝试 findByAuthor_Id
        List<Event> events = findByAuthor_Id(authorId);
        if (events != null && !events.isEmpty()) {
            return events;
        }
        // 如果为空，尝试 findByAuthorId
        return findByAuthorId(authorId);
    }

    List<Event> findAllByTextContainingIgnoreCaseOrIntroductionContainingIgnoreCaseOrNameContainingIgnoreCaseOrTitleContainingIgnoreCase(String text, String introduction, String name, String title);
}
