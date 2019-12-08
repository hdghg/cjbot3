package com.gitlab.hdghg.cjbot3.config;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class RestConfig {

    public static OkHttpClient defaultClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(75L, TimeUnit.SECONDS)
                .readTimeout(75L, TimeUnit.SECONDS)
                .build();
    }

    public static Gson defaultGson() {
        return new Gson();
    }

}
