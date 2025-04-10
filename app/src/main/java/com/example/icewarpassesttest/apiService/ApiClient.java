package com.example.icewarpassesttest.apiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/*
* this class is create as singleton class instance
* only once it will call
* its having converter factory okhttp client and base url of the server*/
public class ApiClient {

    private static final String BASE_URL = "https://mofa.onice.io/teamchatapi/";
    private static ApiService instance = null;

    public static ApiService getClient(){
        if (instance == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();

            instance = retrofit.create(ApiService.class);
        }
        return instance;
    }
}
