package com.ziasy.haanbaba.intellishopping.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class notificationModel {

    @SerializedName("code")
    public String code;

    @SerializedName("response")
    public ArrayList<notificationData>dataList=new ArrayList();
    public class notificationData{
        @SerializedName("id")
        public String id;

        @SerializedName("heading")
        public String heading;

        @SerializedName("description")
        public String description;

        @SerializedName("admin_id")
        public String admin_id;

        @SerializedName("date")
        public String date;

        @SerializedName("status")
        public String status;

        @SerializedName("name")
        public String name;
    }
}
