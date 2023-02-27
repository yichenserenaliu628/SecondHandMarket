package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.secondhandmarketwebapp.entity.Message;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public void sendMessage(Message message) {
        message.setTimestamp(new Date());
        messageRepository.save(message);
    }
    public List<Message> getMessagesBySenderId(int senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    public List<Message> getMessagesByReceiverId(int receiverId) {
        return messageRepository.findByReceiverId(receiverId);
    }
}