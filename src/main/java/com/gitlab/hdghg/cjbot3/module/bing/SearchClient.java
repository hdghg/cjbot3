package com.gitlab.hdghg.cjbot3.module.bing;

import com.gitlab.hdghg.cjbot3.config.RestConfig;
import com.gitlab.hdghg.cjbot3.model.bing.SearchResult;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class SearchClient {

    private static String host = "https://api.cognitive.microsoft.com";
    private static String path = "/bing/v7.0/search";

    public SearchResult searchWeb(String subscriptionKey, String searchQuery) throws IOException {
        Request request = new Request.Builder().url(host + path + "?q=" + searchQuery)
                .header("Ocp-Apim-Subscription-Key", subscriptionKey).get()
                .build();
        Response response = RestConfig.defaultClient().newCall(request).execute();
        ResponseBody body = response.body();
        if (null != body) {
            return RestConfig.defaultGson().fromJson(body.string(), SearchResult.class);
        }
        return null;
    }
}
