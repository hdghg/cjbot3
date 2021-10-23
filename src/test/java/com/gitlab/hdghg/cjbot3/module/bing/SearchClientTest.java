package com.gitlab.hdghg.cjbot3.module.bing;

import com.gitlab.hdghg.cjbot3.model.ChatMessage;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchClientTest {

    @Test
    @Disabled
    void searchRaw() {
        SearchClient searchClient = new SearchClient();
        BingSearchModule m = new BingSearchModule(searchClient, "");
        Message message = mock(Message.class);
        when(message.text()).thenReturn("%b puq");
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(1L);
        when(message.chat()).thenReturn(chat);
        when(message.messageId()).thenReturn(2);
        Optional<ChatMessage> chatMessage = m.processMessage(message);
        assertTrue(chatMessage.isPresent());
        ChatMessage cm = chatMessage.get();
        assertNotNull(cm.message);
    }
}