package com.example.secondhandmarketwebapp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.amazonaws.services.simpleworkflow.model.CloseStatus;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
public class MyWebSocketHandler extends AbstractWebSocketHandler {

    private final Map<WebSocket, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocket webSocket) throws Exception {
        WebSocketSession session = new StandardWebSocketSession(webSocket);
        sessionMap.put(webSocket, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("Hello, " + message.getPayload() + "!"));
    }

    @Override
    public void afterConnectionClosed(WebSocket webSocket, CloseStatus status) throws Exception {
        sessionMap.remove(webSocket);
    }
}