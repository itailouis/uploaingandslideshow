package com.example.reetuc.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientService {

    //private static String BASE_URL = "http:/192.168.2.9/app/";
    private static String BASE_URL = "http://tutorbyte.com/app/";
  private static Retrofit retrofit = null;
  // private static Retrofit retrofit = null;
 static Gson gson = new GsonBuilder()
  .setLenient()
   .create();

  public static Retrofit getClient() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).readTimeout(80, TimeUnit.SECONDS).connectTimeout(80, TimeUnit.SECONDS).addInterceptor(interceptor).build();


    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        //.addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    }
    return retrofit;
  }



}
