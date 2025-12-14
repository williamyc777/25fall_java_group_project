package org.example.backend.app;

import org.example.backend.domain.AbstractUser;
import org.example.backend.domain.Message;
import org.example.backend.dto.AbstractUserDto;
import org.example.backend.dto.MessageDto;
import org.example.backend.service.AbstractUserService;
import org.example.backend.service.MessageService;
import org.example.backend.util.JwtUtil;
import org.example.backend.websocket.WebSocketServer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 该类用于处理消息相关的请求
 * @author Shinomiya
 * @version 1.0
 */
@RestController
@RequestMapping("/message")
public class MessageApp {
    @Autowired
    MessageService messageService;
    @Autowired
    AbstractUserService abstractUserService;

//    private String getSendMessage(User user) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userId", user.getId());
//        jsonObject.put("userName", user.getUsername());
//        return jsonObject.toString();
//    }



    // This method is very stupid, but I don't know how to improve it.
//    private Response handleInvitationError(Message message, Student student, Student friend, String type) {
//        if (message == null) {
//            return new Response(false, 1, "Message not found");
//        }
//        if (message.getTo().getId() != student.getId()) {
//            return new Response(false, 2, "You are not the receiver");
//        }
//        if (!message.getType().equals("Invitation")) {
//            return new Response(false, 3, "Message is not an invitation");
//        }
//        if (type.equals("Accept")) {
//            if (student.getProfile().getSex() != friend.getProfile().getSex()) {
//                return new Response(false, 4, "Your sex are different");
//            }
//            if (student.getProfile().getType() != friend.getProfile().getType()) {
//                return new Response(false, 5, "Your type are different");
//            }
//        }
//        return new Response(true, 0, "success");
//    }

//    private Response handleExchangeError(Message message, Student student, Student friend, String type) {
//        if (message == null) {
//            return new Response(false, 1, "Message not found");
//        }
//        if (message.getTo().getId() != student.getId()) {
//            return new Response(false, 2, "You are not the receiver");
//        }
//        if (!message.getType().equals("Exchange")) {
//            return new Response(false, 3, "Message is not an exchange");
//        }
//        if (type.equals("Accept")) {
//            if (student.getProfile().getSex() != friend.getProfile().getSex()) {
//                return new Response(false, 4, "Your sex are different");
//            }
//            if (student.getProfile().getType() != friend.getProfile().getType()) {
//                return new Response(false, 5, "Your type are different");
//            }
//        }
//        return new Response(true, 0, "success");
//    }

//    @GetMapping("/invAndApp")
//    public List<Message> getInvitations(@RequestHeader("Authorization") String token) {
//        long userId = JwtUtil.getIdByToken(token);
//        return messageService.findMessageByToIdAndType(userId, "Invitation");
//    }

    private String getSendMessage(AbstractUser user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", user.getId());
        jsonObject.put("userName", user.getUsername());
        return jsonObject.toString();
    }

    /**
     * 获取通知
     * @param token 用户token
     * @return 通知列表
     */
    @GetMapping("/notice")
    public List<Message> getNotices(@RequestHeader("Authorization") String token) {
        long userId = JwtUtil.getIdByToken(token);
        return messageService.findMessageByToUserIdAndType(userId, "Notice");
    }

    /**
     * 获取评论
     * @param token 用户token
     * @return 评论列表
     */
    @GetMapping("/chat")
    public List<String> getChatMessages(@RequestHeader("Authorization") String token) {
        long userId = JwtUtil.getIdByToken(token);
        List<Message> fromMessages = messageService.findMessageByFromIdAndType(userId, "Chat");
        List<Message> toMessages = messageService.findMessageByToUserIdAndType(userId, "Chat");
        Map<Long, Boolean> map = new HashMap<>();
        Set<Long> seen = new HashSet<>();
        List<String> result = new ArrayList<>();
        for (Message message : toMessages) {
            if (map.containsKey(message.getFrom().getId())) {
                map.put(message.getFrom().getId(), map.get(message.getFrom().getId()) && message.isRead());
            } else {
                map.put(message.getFrom().getId(), message.isRead());
            }
        }
        for (Message message : fromMessages) {
            if (!map.containsKey(message.getToUser().getId())) {
                map.put(message.getToUser().getId(), true);
            }
        }
        for (Message message : fromMessages) {
            if (seen.contains(message.getToUser().getId())) {
                continue;
            }
            seen.add(message.getToUser().getId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", message.getToUser().getId());
            jsonObject.put("userName", message.getToUser().getName());
            jsonObject.put("hasUnread", !map.get(message.getToUser().getId()));
            result.add(jsonObject.toString());
        }
        for (Message message : toMessages) {
            if (seen.contains(message.getFrom().getId())) {
                continue;
            }
            seen.add(message.getFrom().getId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", message.getFrom().getId());
            jsonObject.put("userName", message.getFrom().getName());
            jsonObject.put("hasUnread", !map.get(message.getFrom().getId()));
            result.add(jsonObject.toString());
        }
        return result;
    }
//    public List<AbstractUserDto> getChatMessages(@RequestParam("id") long userId) {
//        List<Message> fromMessages = messageService.findMessageByFromIdAndType(userId, "Chat");
//        List<Message> toMessages = messageService.findMessageByToUserIdAndType(userId, "Chat");
//        Map<Long, Boolean> map = new HashMap<>();
//        Set<Long> seen = new HashSet<>();
//        List<AbstractUserDto> result = new ArrayList<>();
//        for (Message message : toMessages) {
//            if (map.containsKey(message.getFrom().getId())) {
//                map.put(message.getFrom().getId(), map.get(message.getFrom().getId()) && message.isRead());
//            } else {
//                map.put(message.getFrom().getId(), message.isRead());
//            }
//        }
//        for (Message message : fromMessages) {
//            if (!map.containsKey(message.getToUser().getId())) {
//                map.put(message.getToUser().getId(), true);
//            }
//        }
//        for (Message message : fromMessages) {
//            if (seen.contains(message.getToUser().getId())) {
//                continue;
//            }
//            seen.add(message.getToUser().getId());
//            AbstractUserDto abstractUserDto = new AbstractUserDto();
//            abstractUserDto.setId(message.getToUser().getId());
//            abstractUserDto.setName(message.getToUser().getName());
//            abstractUserDto.setHasUnread(!map.get(message.getToUser().getId()));
//            result.add(abstractUserDto);
//        }
//        for (Message message : toMessages) {
//            if (seen.contains(message.getFrom().getId())) {
//                continue;
//            }
//            seen.add(message.getFrom().getId());
//            AbstractUserDto abstractUserDto = new AbstractUserDto();
//            abstractUserDto.setId(message.getFrom().getId());
//            abstractUserDto.setName(message.getFrom().getName());
//            abstractUserDto.setHasUnread(!map.get(message.getFrom().getId()));
//            result.add(abstractUserDto);
//        }
//        return result;
//    }

    /**
     * 获取聊天记录
     * @param token 用户token
     * @param friendId 好友id
     * @return 聊天记录
     */
    @GetMapping("/chatText")
    public List<MessageDto> getChatText(@RequestHeader("Authorization") String token, @RequestParam("userId") long friendId) {
        long userId = JwtUtil.getIdByToken(token);
        List<Message> fromMessages = messageService.findMessageByFromIdAndToUserIdAndType(userId, friendId, "Chat");
        List<Message> toMessages = messageService.findMessageByFromIdAndToUserIdAndType(friendId, userId, "Chat");
        for (Message message : toMessages) {
            message.setRead(true);
            messageService.updateMessage(message);
        }
        fromMessages.addAll(toMessages);
        fromMessages.sort(Comparator.comparing(Message::getTime));
        return fromMessages.stream().map(this::constructMessageDto).toList();
    }
//    public List<Message> getChatText(@RequestParam("id") long userId, @RequestParam("userId") long friendId) {
//        List<Message> fromMessages = messageService.findMessageByFromIdAndToUserIdAndType(userId, friendId, "Chat");
//        List<Message> toMessages = messageService.findMessageByFromIdAndToUserIdAndType(friendId, userId, "Chat");
//        for (Message message : toMessages) {
//            message.setRead(true);
//            messageService.updateMessage(message);
//        }
//        fromMessages.addAll(toMessages);
//        fromMessages.sort(Comparator.comparing(Message::getTime));
//        return fromMessages;
//    }

    private MessageDto constructMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setFrom(message.getFrom().getId());
        messageDto.setTo(message.getToUser().getId());
        messageDto.setFromName(message.getFrom().getName());
        messageDto.setToName(message.getToUser().getName());
        messageDto.setType(message.getType());
        messageDto.setContent(message.getContent());
        messageDto.setTime(String.valueOf(message.getTime()));
        messageDto.setRead(message.isRead());
        return messageDto;
    }

    /**
     * 发送聊天消息
     * @param token 用户token
     * @param friendId 好友id
     * @param content 消息内容
     * @param time 消息发送时间
     * @return 是否发送成功
     */
    @PostMapping("/sendChat")
    public boolean sendChat(@RequestHeader("Authorization") String token, @RequestParam("userId") long friendId, @RequestParam("content") String content, @RequestParam("time") String time) {
        try {
            long userId = JwtUtil.getIdByToken(token);
            Message message = new Message();
            message.setFrom(abstractUserService.findUserById(userId));
            message.setToUser(abstractUserService.findUserById(friendId));
            message.setType("Chat");
            message.setContent(content);
            message.setTime(LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            message.setRead(false);
            messageService.saveMessage(message);
            WebSocketServer.sendMessageToUser(friendId, getSendMessage(message.getFrom()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
//    public boolean sendChat(@RequestParam("id") long userId, @RequestParam("userId") long friendId, @RequestParam("content") String content, @RequestParam("time") String time) {
//        try {
//            Message message = new Message();
//            message.setFrom(abstractUserService.findUserById(userId));
//            message.setToUser(abstractUserService.findUserById(friendId));
//            message.setType("Chat");
//            message.setContent(content);
//            message.setTime(LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            message.setRead(false);
//            messageService.saveMessage(message);
//            WebSocketServer.sendMessageToUser(friendId, content);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

//    @PostMapping("/invitation/accept")
//    public Response acceptInvitation(@RequestHeader("Authorization") String token, @RequestParam("message_id") long messageId) {
//        try {
//            long userId = JwtUtil.getIdByToken(token);
//            Student student = studentService.findStudentById(userId);
//            Message invitation = messageService.findMessageById(messageId);
//            Student friend = studentService.findStudentById(invitation.getFrom().getId());
//            Response response = handleInvitationError(invitation, student, friend, "Accept");
//            if (response.isResult()) {
//                messageService.deleteMessageById(messageId);
//                List<Student> students = studentService.getStudentByTeamId(friend.getTeam().getId());
//                student.setTeam(friend.getTeam());
//                studentService.updateStudent(student);
//                for (Student s : students) {
//                    Message message = new Message();
//                    message.setFrom(student);
//                    message.setTo(s);
//                    message.setType("Notice");
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("title", "Invitation");
//                    jsonObject.put("content", "Your team has a new member: " + student.getName());
//                    message.setContent(jsonObject.toString());
//                    message.setTime(LocalDateTime.now());
//                    message.setRead(false);
//                    messageService.saveMessage(message);
//                }
//            }
//            return response;
//        } catch (Exception e) {
//            return new Response(new MyException(-10086, e.getMessage()));
//        }
//    }

//    @PostMapping("/invitation/reject")
//    public Response rejectInvitation(@RequestHeader("Authorization") String token, @RequestParam("message_id") long messageId) {
//        try {
//            long userId = JwtUtil.getIdByToken(token);
//            Student student = studentService.findStudentById(userId);
//            Message invitation = messageService.findMessageById(messageId);
//            Student friend = studentService.findStudentById(invitation.getFrom().getId());
//            Response response = handleInvitationError(invitation, student, friend, "Reject");
//            if (response.isResult()) {
//                messageService.deleteMessageById(messageId);
//            }
//            return response;
//        } catch (Exception e) {
//            return new Response(new MyException(-10086, e.getMessage()));
//        }
//    }

    /**
     * 获取评论
     * @param token 用户token
     * @return 评论列表
     */
    @GetMapping("/comment")
    public List<MessageDto> getComment(@RequestHeader("Authorization") String token) {
        long userId = JwtUtil.getIdByToken(token);
        List<Message> toMessages = messageService.findMessageByToUserIdAndType(userId, "Comment");
        return toMessages.stream().map(this::constructMessageDto).toList();
    }

//    @GetMapping("/exchange")
//    public List<Message> getExchange(@RequestHeader("Authorization") String token) {
//        long userId = JwtUtil.getIdByToken(token);
//        List<Message> fromMessages = messageService.findMessageByFromIdAndType(userId, "Exchange");
//        List<Message> toMessages = messageService.findMessageByToIdAndType(userId, "Exchange");
//        fromMessages.addAll(toMessages);
//        return fromMessages;
//    }

    /**
     * @param token 用户token
     * @param type 消息类型
     * @param id 好友id
     * @return 设为已读是否成功
     */
    @PostMapping("/read")
    public boolean readMessages(@RequestHeader("Authorization") String token, @RequestParam("messageType") String type, @RequestParam(value = "userId", defaultValue = "-1") long id) {
        long userId = JwtUtil.getIdByToken(token);
        StringBuilder parsedType = new StringBuilder(type);
        parsedType.setCharAt(0, Character.toUpperCase(parsedType.charAt(0)));
        if (parsedType.toString().equalsIgnoreCase("Chat")) {
            if (id == -1) {
                return false;
            }
            List<Message> messages = messageService.findMessageByFromIdAndToUserIdAndType(id, userId, parsedType.toString());
            for (Message message : messages) {
                message.setRead(true);
                messageService.updateMessage(message);
            }
        } else {
            List<Message> messages = messageService.findMessageByToUserIdAndType(userId, parsedType.toString());
            for (Message message : messages) {
                message.setRead(true);
                messageService.updateMessage(message);
            }
        }
        return true;
    }
//    public boolean readMessages(@RequestParam("id") long userId, @RequestParam("messageType") String type, @RequestParam(value = "userId", defaultValue = "-1") long id) {
//        StringBuilder parsedType = new StringBuilder(type);
//        parsedType.setCharAt(0, Character.toUpperCase(parsedType.charAt(0)));
//        if (parsedType.toString().equalsIgnoreCase("Chat")) {
//            if (id == -1) {
//                return false;
//            }
//            List<Message> messages = messageService.findMessageByFromIdAndToUserIdAndType(id, userId, parsedType.toString());
//            for (Message message : messages) {
//                message.setRead(true);
//                messageService.updateMessage(message);
//            }
//        } else {
//            List<Message> messages = messageService.findMessageByToUserIdAndType(userId, parsedType.toString());
//            for (Message message : messages) {
//                message.setRead(true);
//                messageService.updateMessage(message);
//            }
//        }
//        return true;
//    }

    /**
     * @param token 用户token
     * @return 是否有未读消息
     */
    @GetMapping("/hasAnyUnread")
    public boolean hasAnyUnread(@RequestHeader("Authorization") String token) {
        long userId = JwtUtil.getIdByToken(token);
        return !messageService.findMessageByToUserIdAndRead(userId, false).isEmpty();
    }
//    public boolean hasAnyUnread(@RequestParam("id") long userId) {
//        return !messageService.findMessageByToUserIdAndRead(userId, false).isEmpty();
//    }

    /**
     * @param token 用户token
     * @return 对应的消息类型是否有未读消息
     */
    @GetMapping("/hasUnread")
    public String hasUnread(@RequestHeader("Authorization") String token) {
        long userId = JwtUtil.getIdByToken(token);
        String[] types = {"Chat", "Notice", "Comment"};
        JSONObject jsonObject = new JSONObject();
        for (String type : types) {
            jsonObject.put(type.toLowerCase(), !messageService.findMessageByToUserIdAndTypeAndRead(userId, type, false).isEmpty());
        }
        return jsonObject.toString();
    }
//    public String hasUnread(@RequestParam("id") long userId) {
//        String[] types = {"Chat", "Notice", "Comment"};
//        JSONObject jsonObject = new JSONObject();
//        for (String type : types) {
//            jsonObject.put(type.toLowerCase(), !messageService.findMessageByToUserIdAndTypeAndRead(userId, type, false).isEmpty());
//        }
//        return jsonObject.toString();
//    }

//    @PostMapping("/exchange/accept")
//    public Response acceptExchange(@RequestHeader("Authorization") String token, @RequestParam("message_id") long messageId){
//        long userId = JwtUtil.getIdByToken(token);
//        Student student = studentService.findStudentById(userId);
//        Message exchange = messageService.findMessageById(messageId);
//        Student friend = studentService.findStudentById(exchange.getFrom().getId());
//        Response response = handleExchangeError(exchange, student, friend, "Accept");
//        if (response.isResult()) {
//            messageService.deleteMessageById(messageId);
//            List<Student> students1 = studentService.getStudentByTeamId(student.getTeam().getId());
//            List<Student> students2 = studentService.getStudentByTeamId(friend.getTeam().getId());
//            Room oldRoom = student.getTeam().getRoom();
//            Room newRoom = friend.getTeam().getRoom();
//            friend.getTeam().setRoom(null);
//            studentTeamService.updateStudentTeam(friend.getTeam());
//            student.getTeam().setRoom(newRoom);
//            friend.getTeam().setRoom(oldRoom);
//            studentTeamService.updateStudentTeam(student.getTeam());
//            studentTeamService.updateStudentTeam(friend.getTeam());
//            LocalDateTime now = LocalDateTime.now();
//            for (Student s: students1) {
//                Message message = new Message();
//                message.setFrom(friend);
//                message.setTo(s);
//                message.setType("Notice");
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("title", "Exchange");
//                jsonObject.put("content", "Your room exchange to " + getRoomName(newRoom));
//                message.setContent(jsonObject.toString());
//                message.setRead(false);
//                message.setTime(now);
//                messageService.saveMessage(message);
//            }
//            for (Student s: students2) {
//                Message message = new Message();
//                message.setFrom(student);
//                message.setTo(s);
//                message.setType("Notice");
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("title", "Exchange");
//                jsonObject.put("content", "Your room exchange to " + getRoomName(newRoom));
//                message.setContent(jsonObject.toString());
//                message.setRead(false);
//                message.setTime(now);
//                messageService.saveMessage(message);
//            }
//        }
//        return response;
//    }

//    @PostMapping("/exchange/reject")
//    public Response rejectExchange(@RequestHeader("Authorization") String token, @RequestParam("message_id") long messageId){
//        try {
//            long userId = JwtUtil.getIdByToken(token);
//            Student student = studentService.findStudentById(userId);
//            Message exchange = messageService.findMessageById(messageId);
//            Student friend = studentService.findStudentById(exchange.getFrom().getId());
//            Response response = handleExchangeError(exchange, student, friend, "Reject");
//            if (response.isResult()) {
//                messageService.deleteMessageById(messageId);
//            }
//            return response;
//        } catch (Exception e) {
//            return new Response(new MyException(-10086, e.getMessage()));
//        }
//    }

}
