package com.gitlab.hdghg.cjbot3.module.bing;

import com.gitlab.hdghg.cjbot3.model.ChatMessage;
import com.gitlab.hdghg.cjbot3.model.SearchResults;
import com.gitlab.hdghg.cjbot3.module.Module;
import com.gitlab.hdghg.cjbot3.service.BingWebSearch;
import com.pengrad.telegrambot.model.Message;

import java.util.Optional;

public class BingSearchModule implements Module {

    private final BingWebSearch search;
    private final String subscriptionKey;

    public BingSearchModule(BingWebSearch search, String subscriptionKey) {
        this.search = search;
        this.subscriptionKey = subscriptionKey;
    }

    @Override
    public Optional<ChatMessage> processMessage(Message message) {
        String text = message.text();
        if (null == text) {
            return Optional.empty();
        }
        if (!text.startsWith("%и ") && !text.startsWith("%b ")) {
            return Optional.empty();
        }
        String query = text.substring(3);
        SearchResults searchResults;
        try {
            searchResults = search.searchWeb(subscriptionKey, query);
        } catch (Exception e) {
            return Optional.empty();
        }
        String substring = searchResults.jsonResponse.substring(0, 100);
        return Optional.of(new ChatMessage(message.chat().id(), substring, null));
    }
}
