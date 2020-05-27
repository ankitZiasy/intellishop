package com.ziasy.haanbaba.intellishopping.bluetooth;

public class Wallet {
    public static Wallet wallet = null;
    String address;
    String secret;

    public static Wallet getInstance() {
        if (wallet == null) {
            wallet = new Wallet();
        }
        return wallet;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAddress() {
        return this.address;
    }

    public String getSecret() {
        return this.secret;
    }
}
