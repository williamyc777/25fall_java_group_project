package org.example.backend.service;

import org.example.backend.domain.Message;

import java.util.List;

public interface MessageService {
    boolean saveMessage(Message message);

    boolean deleteMessageById(long id);

    boolean updateMessage(Message message);

    Message findMessageById(long id);

    List<Message> findMessageByType(String type);

    List<Message> findAllMessage();

    List<Message> findMessageByFromId(long id);

    List<Message> findMessageByToUserId(long id);

    List<Message> findMessageByFromIdAndToUserId(long fromId, long toId);

    List<Message> findMessageByFromIdAndToUserIdAndType(long fromId, long toId, String type);

    List<Message> findMessageByFromIdAndType(long fromId, String type);

    List<Message> findMessageByToUserIdAndType(long toId, String type);

    List<Message> findMessageByToUserIdAndRead(long toId, boolean read);

    List<Message> findMessageByToUserIdAndTypeAndRead(long toId, String type, boolean read);
}
