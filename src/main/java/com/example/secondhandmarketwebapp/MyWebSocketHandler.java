package com.example.secondhandmarketwebapp;

import com.amazonaws.services.connect.model.ChatMessage;
import com.example.secondhandmarketwebapp.dao.MessageDao;
import com.example.secondhandmarketwebapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageDao messageDao;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ChatMessage chatMessage = mapper.readValue(message.getPayload(), ChatMessage.class);
        if (userService.isAuthorizedToMessage(chatMessage.getSenderId(), chatMessage.getRecipientId())) {
            messageRepository.save(chatMessage);
            messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages", chatMessage);
        }
    }
}