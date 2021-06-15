package com.gitlab.hdghg.cjbot3.service;

import com.gitlab.hdghg.cjbot3.model.ChatMessage;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageServiceTest {

    @Test
    void toSendMessage() {
        var messageService = mock(MessageService.class);
        when(messageService.toSendMessage(any())).thenCallRealMethod();

        var msg = new ChatMessage(123L, "msg", null);
        SendMessage sendMessage = messageService.toSendMessage(msg);
        Map<String, Object> parameters = sendMessage.getParameters();
        assertEquals(123L, parameters.get("chat_id"));
        assertEquals("msg", parameters.get("text"));
        assertEquals(null, parameters.get("reply_to_message_id"));

    }
}
