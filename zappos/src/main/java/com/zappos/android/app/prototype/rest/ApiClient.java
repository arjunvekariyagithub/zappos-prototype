package com.zappos.android.app.prototype.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "https://api.zappos.com/";
    private static Retrofit retrofit = null;

    /**
     * Builds retrofit HTTP client
     * Using {@link GsonConverterFactory} for data serialization
     */

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
