package com.gitlab.hdghg.cjbot3.config;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for rest related API
 */
public class RestConfig {

    private static OkHttpClient client;
    private static Gson gson;

    public static OkHttpClient defaultClient() {
        if (null == client) {
            synchronized (RestConfig.class) {
                if (null == client) {
                    client = new OkHttpClient.Builder()
                            .connectTimeout(75L, TimeUnit.SECONDS)
                            .readTimeout(75L, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return client;
    }

    public static Gson defaultGson() {
        if (null == gson) {
            synchronized (RestConfig.class) {
                if (null == gson) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}
