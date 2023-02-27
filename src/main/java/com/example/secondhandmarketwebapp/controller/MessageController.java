package com.example.secondhandmarketwebapp.controller;

import com.amazonaws.services.connect.model.ChatMessage;
import com.example.secondhandmarketwebapp.SimpMessagingTemplate;
import com.example.secondhandmarketwebapp.service.MessageService;
import com.example.secondhandmarketwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat/{receiverId}")
    public void sendChatMessage(@PathVariable int receiverId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        int senderId = userService.getCurrentUserId();
        message.setSenderId(senderId);
        chatMessage.setReceiverId(receiverId);
        messageService.sendMessage(chatMessage.toMessage());
        messagingTemplate.convertAndSendToUser(receiverId.toString(), "/queue/messages", chatMessage);
    }
    @MessageMapping("/chat/{senderId}/{receiverId}")
    public void sendChatMessageToUser(@PathVariable int senderId, @PathVariable Long receiverId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        messageService.sendMessage(message.toMessage());
        messagingTemplate.convertAndSendToUser(receiverId.toString(), "/queue/messages", chatMessage);
        messagingTemplate.convertAndSendToUser(senderId.toString(), "/queue/messages", chatMessage);
    }
}