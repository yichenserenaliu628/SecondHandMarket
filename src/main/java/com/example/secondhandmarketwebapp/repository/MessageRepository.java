package com.example.secondhandmarketwebapp.repository;

import com.example.secondhandmarketwebapp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderId(int senderId);
    List<Message> findByReceiverId(int receiverId);

    void save(Message message);
}

