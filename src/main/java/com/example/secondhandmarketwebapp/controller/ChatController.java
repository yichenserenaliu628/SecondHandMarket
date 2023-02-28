package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.RequestMapping;
>>>>>>> f7f1e5b70efdd7279437bb5f89e400b022983481

@Controller
public class ChatController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/message") // /app/message
	@SendTo("/chatroom/public")
	public Message receiveMessage(@Payload Message message){
		return message;
	}

	@MessageMapping("/private-message")
	public Message recMessage(@Payload Message message){
		// listner messages from David: /user/David/private
		simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
		System.out.println(message.toString());
		return message;
	}
}