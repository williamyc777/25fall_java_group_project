package org.example.backend.api;

import org.example.backend.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findMessageByType(String type);

    List<Message> findMessageByFromId(long id);

    List<Message> findMessageByToUserId(long id);

    List<Message> findMessageByFromIdAndToUserId(long fromId, long toId);

    List<Message> findMessageByFromIdAndType(long fromId, String type);

    List<Message> findMessageByToUserIdAndType(long toId, String type);

    List<Message> findMessageByFromIdAndToUserIdAndType(long fromId, long toId, String type);

    List<Message> findMessageByToUserIdAndRead(long toId, boolean read);

    List<Message> findMessageByToUserIdAndTypeAndRead(long toId, String type, boolean read);
}
