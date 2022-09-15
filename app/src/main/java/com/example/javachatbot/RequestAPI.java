package com.example.javachatbot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RequestAPI {
    @GET
    Call<MsgModal> getMessage(@Url String url);
}
