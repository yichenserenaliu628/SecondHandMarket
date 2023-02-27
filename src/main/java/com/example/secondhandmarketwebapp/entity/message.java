package com.example.secondhandmarketwebapp.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "message")
public class message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @Column(name = "senderId")
    private User sender;

    @ManyToOne
    @Column(name = "receiverId")
    private User receiver;
    private String message;
    private LocalDateTime time;

}