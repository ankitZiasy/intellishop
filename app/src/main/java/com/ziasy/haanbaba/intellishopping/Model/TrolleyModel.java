package com.ziasy.haanbaba.intellishopping.Model;

public class TrolleyModel {
    String id,trolleyQrcode;

    public TrolleyModel(String id, String trolleyQrcode) {
        this.id = id;
        this.trolleyQrcode = trolleyQrcode;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrolleyQrcode() {
        return trolleyQrcode;
    }

    public void setTrolleyQrcode(String trolleyQrcode) {
        this.trolleyQrcode = trolleyQrcode;
    }
}
