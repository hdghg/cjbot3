package com.gitlab.hdghg.cjbot3;

import com.gitlab.hdghg.cjbot3.service.MessageService;

public class CjbotApp {

    public static void main(String[] args) {
        MessageService.forEnvironment()
                .startPolling();
    }
}
