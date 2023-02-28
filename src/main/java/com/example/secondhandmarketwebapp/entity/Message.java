package com.example.secondhandmarketwebapp.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {
	private String senderName;
	private String receiverName;
	private String message;
	private String date;
	private Status status;
}

