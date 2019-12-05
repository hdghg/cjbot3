package com.gitlab.hdghg.cjbot3.service;

import com.gitlab.hdghg.cjbot3.module.puk.PukModule;
import com.microsoft.azure.functions.ExecutionContext;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Optional;

public class MessageService {

    private final TelegramBot telegramBot;
    private final PukModule pukModule;

    public MessageService(String token, int myId) {
        telegramBot = new TelegramBot.Builder(token).build();
        this.pukModule = new PukModule(myId);
    }

    public void processUpdate(Update update, ExecutionContext context) {
        context.getLogger().info("Processing update " + update.updateId());
        Optional.ofNullable(update.message())
                .flatMap(pukModule::processMessage)
                .map(cm -> new SendMessage(cm.chat, cm.message).replyToMessageId(cm.replyToMessageId))
                .ifPresent(telegramBot::execute);
    }
}
