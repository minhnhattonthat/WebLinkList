package com.nathan.app.weblinklist.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitService {

    @GET
    Call<String> getWebLink(@Url String url);
}
