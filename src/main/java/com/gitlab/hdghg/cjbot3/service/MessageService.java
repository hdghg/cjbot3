package com.gitlab.hdghg.cjbot3.service;

import com.gitlab.hdghg.cjbot3.module.Module;
import com.gitlab.hdghg.cjbot3.module.bing.BingSearchModule;
import com.gitlab.hdghg.cjbot3.module.puk.PukModule;
import com.microsoft.azure.functions.ExecutionContext;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Optional;
import java.util.stream.Stream;

public class MessageService {

    private final TelegramBot telegramBot;
    private final PukModule pukModule;
    private final BingSearchModule bingSearchModule;

    public MessageService(String token, int myId, String searchKey) {
        telegramBot = new TelegramBot.Builder(token).build();
        this.pukModule = new PukModule(myId);
        this.bingSearchModule = new BingSearchModule(new BingWebSearch(), searchKey);
    }

    public void processUpdate(Update update, ExecutionContext context) {
        context.getLogger().info("Processing update " + update.updateId());
        Optional.ofNullable(update.message())
                .ifPresent(this::processMessage);
    }

    public void startPolling() {
        telegramBot.setUpdatesListener(list -> {
            list.forEach(update -> Optional.ofNullable(update.message())
                    .ifPresent(MessageService.this::processMessage));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void processMessage(Message message) {
        Stream<Module> modules = Stream.of(this.bingSearchModule, pukModule);
        modules.map(m -> m.processMessage(message))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .map(cm -> {
                    SendMessage sm = new SendMessage(cm.chat, cm.message);
                    Optional.ofNullable(cm.replyToMessageId)
                            .ifPresent(sm::replyToMessageId);
                    return sm;

                })
                .ifPresent(telegramBot::execute);
    }
}
