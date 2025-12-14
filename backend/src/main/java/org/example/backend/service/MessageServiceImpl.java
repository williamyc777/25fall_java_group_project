package org.example.backend.service;

import org.example.backend.api.MessageRepository;
import org.example.backend.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    MessageRepository messageRepository;

    @Override
    public boolean saveMessage(Message message) {
        messageRepository.save(message);
        return true;
    }

    @Override
    public boolean deleteMessageById(long id) {
        messageRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateMessage(Message message) {
        messageRepository.save(message);
        return true;
    }

    @Override
    public Message findMessageById(long id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public List<Message> findMessageByType(String type) {
        return messageRepository.findMessageByType(type);
    }

    @Override
    public List<Message> findAllMessage() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> findMessageByFromId(long id) {
        return messageRepository.findMessageByFromId(id);
    }

    @Override
    public List<Message> findMessageByToUserId(long id) {
        return messageRepository.findMessageByToUserId(id);
    }

    @Override
    public List<Message> findMessageByFromIdAndToUserId(long fromId, long toId) {
        return messageRepository.findMessageByFromIdAndToUserId(fromId, toId);
    }

    @Override
    public List<Message> findMessageByFromIdAndToUserIdAndType(long fromId, long toId, String type) {
        return messageRepository.findMessageByFromIdAndToUserIdAndType(fromId, toId, type);
    }

    @Override
    public List<Message> findMessageByFromIdAndType(long fromId, String type) {
        return messageRepository.findMessageByFromIdAndType(fromId, type);
    }

    @Override
    public List<Message> findMessageByToUserIdAndType(long toId, String type) {
        return messageRepository.findMessageByToUserIdAndType(toId, type);
    }

    @Override
    public List<Message> findMessageByToUserIdAndRead(long toId, boolean read) {
        return messageRepository.findMessageByToUserIdAndRead(toId, read);
    }

    @Override
    public List<Message> findMessageByToUserIdAndTypeAndRead(long toId, String type, boolean read) {
        return messageRepository.findMessageByToUserIdAndTypeAndRead(toId, type, read);
    }
}
