package com.adivid.zpattendanceadmin.network;

import com.adivid.zpattendanceadmin.interfaces.Api2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient2 {

    private static final String BASE_URL2 = "https://nashikzp.thethirdeyeindia.com/api/app/master/";
    private static RetrofitClient2 mInstance;
    private Retrofit retrofit;

    private RetrofitClient2() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient2 getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient2();
        }
        return mInstance;
    }

    public Api2 getApiTwo() {
        return retrofit.create(Api2.class);
    }
}
