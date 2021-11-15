package com.example.userapi;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {

    @Multipart
    @POST("register.php")
    Call<ResponsPojo> databody(
            @Part MultipartBody.Part profile_image,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );

    @POST("updateuser.php")
    Call<ResponsPojo> getupdateuser(
            @Query("id") int userid
    );

    @Multipart
    @POST("register.php")
    Call<ResponsPojo> register(
            @Part("username") String username,
            @Part("email") String email,
            @Part("password") String password,
            @Part MultipartBody.Part profile_image
    );

    @POST("login.php")
    @FormUrlEncoded
    Call<LoginResponse> login(
            @FieldMap Map<String,String> user
            );
}
