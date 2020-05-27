package com.ziasy.haanbaba.intellishopping.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductDataModel {
    @SerializedName("code")
    public String code;
    @SerializedName("response")
    public ArrayList<ProductData> dataList = new ArrayList();

    public class ProductData {
        @SerializedName("id")
        public String id;
        @SerializedName("retailer_id")
        public String retailer_id;
        @SerializedName("name")
        public String name;
        @SerializedName("amount")
        public String amount;
        @SerializedName("qrcode")
        public String qrcode;
        @SerializedName("weight")
        public String weight;
        @SerializedName("image")
        public String image;
    }
}
