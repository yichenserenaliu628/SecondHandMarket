package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Message;
import com.example.secondhandmarketwebapp.SimpMessagingTemplate;
import com.example.secondhandmarketwebapp.service.MessageService;
import com.example.secondhandmarketwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public void send(Message message) throws Exception {
        //String time = new SimpleDateFormat("HH:mm").format(new Date());
        messageService.sendMessage(message);
    }
}