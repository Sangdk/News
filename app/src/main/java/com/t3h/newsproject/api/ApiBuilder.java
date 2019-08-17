package com.t3h.newsproject.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {
    private static Api api;

    public static Api getInstance() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(Api.class);
        }
        return api;
    }
}
