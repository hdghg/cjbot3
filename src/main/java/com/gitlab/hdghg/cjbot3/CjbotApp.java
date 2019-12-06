package com.gitlab.hdghg.cjbot3;

import com.gitlab.hdghg.cjbot3.service.MessageService;

public class CjbotApp {

    public static void main(String[] args) throws Exception {
        String key = System.getenv("bot.key");
        int id = Integer.parseInt(System.getenv("bot.id"));
        String searchKey = System.getenv("bot.bing.key");
        new MessageService(key, id, searchKey)
                .startPolling();
    }
}
