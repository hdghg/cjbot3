package com.gitlab.hdghg.cjbot3.module.bing;

import com.gitlab.hdghg.cjbot3.model.ChatMessage;
import com.gitlab.hdghg.cjbot3.model.bing.SearchResult;
import com.gitlab.hdghg.cjbot3.model.bing.Value;
import com.gitlab.hdghg.cjbot3.module.Module;
import com.pengrad.telegrambot.model.Message;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class BingSearchModule implements Module {

    private static final Logger log = Logger.getLogger(BingSearchModule.class.getName());

    private final SearchClient search;
    private final String subscriptionKey;

    public BingSearchModule(SearchClient search, String subscriptionKey) {
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
        SearchResult searchResult = search.searchWeb(subscriptionKey, query);
        return valueFromResult(searchResult, query)
                .map(v -> new ChatMessage(message.chat().id(), v, message.messageId()));
    }

    Optional<String> valueFromResult(SearchResult searchResult, String query) {
        if (null == searchResult) {
            log.warning("null searchResult for query: '" + query + "'");
            return Optional.empty();
        }
        if (null == searchResult.getWebPages()) {
            log.warning("SearchResult has no 'webPages' for query: '" + query
                    + "'. The error is " + searchResult.getError());
            return Optional.empty();
        }
        List<Value> valueList = searchResult.getWebPages().getValue();
        if (null == valueList) {
            log.warning("SearchResult.webPages has no 'value' for query: '" + query + "'");
            return Optional.empty();
        }
        if (0 == valueList.size()) {
            log.warning("SearchResult.webPages.value is empty for query: '" + query + "'");
            return Optional.empty();
        }
        return Optional.of(valueList.get(0))
                .map(v -> v.getUrl() + "\n" + v.getName() + "\n" + v.getSnippet());
    }
}
