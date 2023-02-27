package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.MessageDao;
import com.example.secondhandmarketwebapp.entity.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    public void sendMessage(Message message) {
        message.setTimestamp(new Date());
        messageDao.save(message);
    }
    public String getMessagesBySenderId(int senderId) {
        return messageDao.findBySenderId(senderId);
    }

    public String getMessagesByReceiverId(int receiverId) {
        return messageDao.findByReceiverId(receiverId);
    }
}