package com.ziasy.haanbaba.intellishopping.bluetooth;

public class Device {
    public String address;
    public String id;
    public String name;

    public Device(String name, String id, String address) {
        this.name = name;
        this.id = id;
        this.address = address;
    }
}
