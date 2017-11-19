package com.ram.github_api_android_sample.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rmreddy on 19/11/17.
 */

public class ApiService {

    public ApiService(){
    }

    public String post(String requestUrl) throws IOException{
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        MediaType media = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .addHeader("content-Type", "application/json")
                .build();

        OkHttpClient client = httpClient.build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        return responseString;
    }

}
