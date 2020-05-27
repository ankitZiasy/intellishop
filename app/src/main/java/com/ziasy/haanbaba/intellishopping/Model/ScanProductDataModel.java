package com.ziasy.haanbaba.intellishopping.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ScanProductDataModel {
    @SerializedName("code")
    public String code;
    @SerializedName("response")
    public ArrayList<ScanProductData> dataList = new ArrayList();

    public class ScanProductData {
        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("name")
        public String name;
        @SerializedName("amount")
        public String amount;
        @SerializedName("qrcode")
        public String qrcode;
        @SerializedName("weight")
        public String weight;
        @SerializedName("description")
        public String description;

        @SerializedName("quantity")
        public String quantity;

        @SerializedName("itemid")
        public String itemid;

        @SerializedName("picture")
        public String picture;

        @SerializedName("date")
        public String date;

        @SerializedName("status")
        public String status;
    }

}