package com.gitlab.hdghg.cjbot3.module.bing;

import com.gitlab.hdghg.cjbot3.model.ChatMessage;
import com.gitlab.hdghg.cjbot3.model.bing.SearchResult;
import com.gitlab.hdghg.cjbot3.module.Module;
import com.pengrad.telegrambot.model.Message;

import java.util.Optional;

public class BingSearchModule implements Module {

    private final BingSearchService search;
    private final String subscriptionKey;

    public BingSearchModule(BingSearchService search, String subscriptionKey) {
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
        SearchResult searchResults;
        try {
            searchResults = search.searchWeb(subscriptionKey, query);
        } catch (Exception e) {
            return Optional.empty();
        }
        String url = searchResults.getWebPages().getValue().get(0).getUrl();
        String name = searchResults.getWebPages().getValue().get(0).getName();
        String result = name + "\n" + url;
        return Optional.of(new ChatMessage(message.chat().id(), result, message.messageId()));
    }
}