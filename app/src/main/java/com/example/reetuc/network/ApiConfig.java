package com.example.reetuc.network;

import com.example.reetuc.models.ServerImages;
import com.example.reetuc.models.ServerResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiConfig {

  @Multipart
  @POST("upload.php")
  Call<ServerResponse> uploadFile(@Query("id") int id, @Part MultipartBody.Part file, @Part("file") RequestBody name);

  @Multipart
  @POST("upload_multiple_files.php")
  Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1,@Part MultipartBody.Part file2);

  @GET("index.json")
  Call<List<ServerImages>> getImages();
}
