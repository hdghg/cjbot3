package com.gitlab.hdghg.cjbot3;

import com.gitlab.hdghg.cjbot3.service.MessageService;

public class CjbotApp {

    public static void main(String[] args) {
        String key = System.getenv("bot.key");
        int id = Integer.parseInt(System.getenv("bot.id"));
        new MessageService(key, id)
                .startPolling();
    }
}
