package com.example.secondhandmarketwebapp.dao;

import com.amazonaws.services.connect.model.ChatMessage;
import com.example.secondhandmarketwebapp.entity.message;

import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    List<String> findBySenderIdAndRecipientId(int senderId, int recipientId) {
        return new ArrayList<>();
    }
}
