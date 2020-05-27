package com.ziasy.haanbaba.intellishopping.Model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("code")
    public String code;
    @SerializedName("response")
    public String response;
    @SerializedName("status")
    public String status;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("mobile")
    public String mobile;
}
