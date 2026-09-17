package com.zdkk.speed.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 服务
 */
@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {
    private static Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 连接建立成功
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("WebSocket onOpen, sid: {}", sid);
        sessionMap.put(sid, session);
    }

    /**
     * 收到客户端消息
     * @param message
     * @param sid
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("WebSocket onMessage, sid: {}, message: {}", sid, message);
    }

    /**
     * 连接关闭
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("WebSocket onClose, sid: {}", sid);
        sessionMap.remove(sid);
    }

    /**
     * 发送消息给所有客户端
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        }
    }

}
