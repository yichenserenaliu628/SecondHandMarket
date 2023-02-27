package com.example.secondhandmarketwebapp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.amazonaws.services.dynamodbv2.xspec.S;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

}

