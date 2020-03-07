package com.nathan.app.weblinklist.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitClient {

    private static RetrofitService apiService = null;

    private RetrofitClient() {

    }

    private static Retrofit initRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl("https://example.com/")
                .addConverterFactory(new StringConverterFactory())
                .client(okHttpClient)
                .build();
    }

    public static RetrofitService getApiService() {
        if (apiService == null) {
            synchronized (RetrofitClient.class) {
                if (apiService == null) {
                    apiService = initRetrofit().create(RetrofitService.class);
                }
            }
        }

        return apiService;
    }
}
