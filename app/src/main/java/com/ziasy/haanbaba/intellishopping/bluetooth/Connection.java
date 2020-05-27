package com.ziasy.haanbaba.intellishopping.bluetooth;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by ANDROID on 17-May-18.
 */

public class Connection {

    private static boolean state = false;

    public static boolean blueTooth() {

        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!bluetooth.isEnabled()) {
            System.out.println("Bluetooth is Disable...");
            state = true;
        } else if (bluetooth.isEnabled()) {
            String address = bluetooth.getAddress();
            String name = bluetooth.getName();
            System.out.println(name + " : " + address);
            state = false;
        }
        return state;
    }

}
