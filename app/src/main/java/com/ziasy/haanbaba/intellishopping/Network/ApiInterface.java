package com.ziasy.haanbaba.intellishopping.Network;

import com.google.gson.JsonObject;
import com.ziasy.haanbaba.intellishopping.Model.LoginModel;
import com.ziasy.haanbaba.intellishopping.Model.ProductDataModel;
import com.ziasy.haanbaba.intellishopping.Model.ScanProductDataModel;
import com.ziasy.haanbaba.intellishopping.Model.SignUpModelMunafa;
import com.ziasy.haanbaba.intellishopping.Model.addFamilyModel;
import com.ziasy.haanbaba.intellishopping.Model.familyModel;
import com.ziasy.haanbaba.intellishopping.Model.notificationModel;
import com.ziasy.haanbaba.intellishopping.Model.signUpModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("mobileapp/login")
    Call<LoginModel> getLoginUp(@Query("email") String email, @Query("password") String password, @Query("imei") String imei, @Query("device_id") String device_id);

    @POST("mobileapp/signup")
    Call<signUpModel> getSignUp(@Query("name") String name, @Query("email") String email, @Query("password") String password, @Query("mobile") String mobile, @Query("dob") String dob, @Query("imei") String imei, @Query("device_id") String device_id);

    @POST("mobileapp/productList")
    Call<ProductDataModel> getProductList(@Body JsonObject jsonObject);

    @POST("mobileapp/viewNotification")
    Call<notificationModel> getNotificationList(@Body JsonObject jsonObject);

    @POST("mobileapp/familyMemberList")
    Call<familyModel> getFamilyList(@Body JsonObject jsonObject);

    @POST("mobileapp/familyMember")
    Call<addFamilyModel> addFamilyMember(@Body JsonObject jsonObject);
/*
  @POST("apps/registration_user.php")
    Call<SignUpModelMunafa> getSignUpMunafa(@Body JsonObject jsonObject);*/

    @POST("mobileapp/getProductDetails")
    Call<ScanProductDataModel> getScanProductDetails(@Body JsonObject jsonObject);

}
