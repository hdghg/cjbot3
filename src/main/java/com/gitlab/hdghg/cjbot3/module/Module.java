package com.gitlab.hdghg.cjbot3.module;

import com.gitlab.hdghg.cjbot3.model.ChatMessage;
import com.pengrad.telegrambot.model.Message;

import java.util.Optional;

public interface Module {

    Optional<ChatMessage> processMessage(Message message);
}
