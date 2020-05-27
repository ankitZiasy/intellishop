package com.ziasy.haanbaba.intellishopping.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class familyModel {
    @SerializedName("code")
    public String code;

    @SerializedName("response")
    public ArrayList<familyDataList> dataList = new ArrayList();

    public static class familyDataList {
        @SerializedName("id")
        public String id;

        @SerializedName("user_id")
        public String user_id;

        @SerializedName("name")
        public String name;

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("gender")
        public String gender;

        @SerializedName("relation")
        public String relation;

        @SerializedName("date")
        public String date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @SerializedName("status")
        public String status;
    }
}
