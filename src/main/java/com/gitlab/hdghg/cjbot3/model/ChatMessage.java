package com.gitlab.hdghg.cjbot3.model;

public class ChatMessage {

    public final Long chat;
    public final String message;
    public final Integer replyToMessageId;

    public ChatMessage(Long chat, String message, Integer replyToMessageId) {
        this.chat = chat;
        this.message = message;
        this.replyToMessageId = replyToMessageId;
    }
}
