package org.example.backend.websocket;


import com.google.gson.Gson;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.example.backend.service.MessageService;
import org.example.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@ServerEndpoint("/websocket/{token}")
@Component
public class WebSocketServer {
    private Session session;

    private static final ConcurrentHashMap<Long, Session> sessionPool = new ConcurrentHashMap<>();

    private static final CopyOnWriteArrayList<WebSocketServer> webSockets = new CopyOnWriteArrayList<>();

    private static final Logger logger = Logger.getLogger(WebSocketServer.class.getName());

    private static MessageService messageService;

    private static final Gson gson = new Gson();

    @Autowired
    public void setChatService(MessageService messageService) {
        WebSocketServer.messageService = messageService;
    }

    /**
     * 建立WebSocket连接
     *
     * @param session
     * @param token   用户token信息
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        long userId = JwtUtil.getIdByToken(token);
        logger.info(String.format("WebSocket建立连接中,连接用户ID：{%d}", userId));
        Session historySession = sessionPool.get(userId);
        if (historySession != null) {
            try {
                sessionPool.remove(userId);
                historySession.close();
                webSockets.spliterator().forEachRemaining(wss -> {
                    if (wss.session.equals(historySession)) webSockets.remove(wss);
                });
            } catch (IOException e) {
                logger.severe(String.format("关闭历史连接发生错误：" + e.getMessage()));
            }
        }
        // 建立连接
        this.session = session;
        webSockets.addIfAbsent(this);
        sessionPool.put(userId, session);
        logger.info(String.format("建立连接完成,当前在线人数为：{%d}", webSockets.size()));
        //TODO: 推送未读消息
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        logger.severe(String.format("WebSocket发生错误,错误信息：%s", throwable.getMessage()));
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        webSockets.remove(this);
        logger.info(String.format("连接关闭,当前在线人数为：{%d}", webSockets.size()));
    }

    /**
     * 接收客户端消息
     *
     * @param message 接收的消息
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info(String.format("接收到客户端发来的消息：%s", message));
//        String[] messages = message.split(" ");
//        sendMessageToUser(Long.parseLong(messages[0]), messages[1]);
    }

    /**
     * 推送消息到指定用户
     *
     * @param userId  用户ID
     * @param message 发送的消息
     */
    public static void sendMessageToUser(long userId, String message) {
        logger.info(String.format("被推送的用户ID：" + userId + ",推送内容：" + message));
        Session session = sessionPool.get(userId);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.severe(String.format("推送消息到指定用户发生错误：" + e.getMessage()));
            }
        }
    }
}
