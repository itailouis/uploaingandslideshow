package com.example.reetuc;

import android.app.Application;

import com.example.reetuc.network.ApiConfig;
import com.example.reetuc.network.ClientService;

public class App extends Application {

  private static ApiConfig endPoints;
  public static String BASE_IMAGE_URL = "http:/192.168.2.9/app/vegetables/";


  public static ApiConfig getEndPoints() {
    return endPoints;
  }
  @Override
  public void onCreate() {
    super.onCreate();
    endPoints = ClientService.getClient().create(ApiConfig.class);
  }

}
