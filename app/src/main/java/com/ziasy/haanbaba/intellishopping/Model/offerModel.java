package com.ziasy.haanbaba.intellishopping.Model;

public class offerModel {

    Integer icon ;
    String Title,message ;

    public offerModel(Integer icon, String title, String message) {
        this.icon = icon;
        Title = title;
        this.message = message;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
