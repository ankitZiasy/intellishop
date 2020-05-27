package com.ziasy.haanbaba.intellishopping.bluetooth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.bluetooth.Device;

import java.util.ArrayList;


public class DeviceListAdapter extends ArrayAdapter<Device> {
    ArrayList<Device> device;

    public DeviceListAdapter(Context context, ArrayList<Device> device) {
        super(context, 0, device);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Device list = (Device) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_connected_item, parent, false);
        }
        TextView tvHome = (TextView) convertView.findViewById(R.id.device_id);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.device_address);
        ((TextView) convertView.findViewById(R.id.device_name)).setText(list.name);
        tvHome.setText(list.id);
        tvAddress.setText(list.address);
        return convertView;
    }
}
