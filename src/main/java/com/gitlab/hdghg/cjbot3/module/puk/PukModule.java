package com.gitlab.hdghg.cjbot3.module.puk;

import com.gitlab.hdghg.cjbot3.model.ChatMessage;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;

import java.util.Optional;
import java.util.Random;

/**
 * Simple ping module
 */
public class PukModule {

    private static final String[] ANS = {"пук", "кек", "лал", "лек", "лел", "puk", "kjk", "kek", "ktk", "rtr"};
    private static final int SIZE = ANS.length;

    private Random rand = new Random();

    private final int botId;

    public PukModule(int botId) {
        this.botId = botId;
    }

    public Optional<ChatMessage> processMessage(Message message) {
        Message replyToMessage = message.replyToMessage();
        if (null == replyToMessage) {
            return Optional.empty();
        }
        User from = replyToMessage.from();
        if (null == from) {
            return Optional.empty();
        }
        Integer replyToUserId = from.id();
        if (null == replyToUserId) {
            return Optional.empty();
        }
        if (botId != replyToUserId) {
            return Optional.empty();
        }
        return Optional.of(new ChatMessage(replyToMessage.chat().id(), ANS[rand.nextInt(SIZE)], message.messageId()));
    }
}