package com.gitlab.hdghg.cjbot3.module.bing;

import com.gitlab.hdghg.cjbot3.config.RestConfig;
import com.gitlab.hdghg.cjbot3.model.bing.SearchResult;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.logging.Logger;

public class SearchClient {

    private static final Logger log = Logger.getLogger(SearchClient.class.getName());

    private static String host = "api.cognitive.microsoft.com";
    private static String path = "bing/v7.0/search";

    private static HttpUrl baseSearchUrl = new HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .addPathSegments(path)
            .addQueryParameter("count", "1")
            .addQueryParameter("responseFilter", "Webpages")
            .addQueryParameter("textDecorations", "true")
            .addQueryParameter("textFormat", "HTML")
            .build();

    public SearchResult searchWeb(String subscriptionKey, String searchQuery) {
        String body = searchRaw(subscriptionKey, searchQuery);
        if (null != body) {
            return RestConfig.defaultGson().fromJson(body, SearchResult.class);
        }
        return null;
    }

    String searchRaw(String subscriptionKey, String searchQuery) {
        HttpUrl url = baseSearchUrl.newBuilder()
                .addQueryParameter("q", searchQuery)
                .build();
        Request request = new Request.Builder().url(url)
                .header("Ocp-Apim-Subscription-Key", subscriptionKey).get()
                .build();
        try {
            var response = RestConfig.defaultClient().newCall(request).execute();
            if (200 != response.code()) {
                log.warning("Search status code is abnormal: " + response.code());
            }
            ResponseBody body = response.body();
            if (null != body) {
                return body.string();
            }
        } catch (IOException e) {
            log.warning("IOException when performing search request " + e.getMessage());
        }
        return null;
    }
}
