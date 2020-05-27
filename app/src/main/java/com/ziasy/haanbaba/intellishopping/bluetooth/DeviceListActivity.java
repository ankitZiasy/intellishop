package com.ziasy.haanbaba.intellishopping.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ziasy.haanbaba.intellishopping.R;

import java.util.Set;

public class DeviceListActivity extends Activity {
    public static String DEVICE_ADDRESS = "deviceAddress";
    public final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    private BluetoothAdapter bluetoothAdapter;
    private Button btnDeviceListScan;
    private final BroadcastReceiver discoveryFinishReceiver = new Device285();
    private ListView lvDeviceListNewDevice;
    private ListView lvDeviceListPairedDevice;
    private OnItemClickListener mDeviceClickListener = new Device252();
    private OnItemClickListener mNewDeviceClickListener = new Device263();
    private final BroadcastReceiver mPairReceiver = new Device274();
    private ArrayAdapter<String> newDevicesArrayAdapter;
    private ArrayAdapter<String> pairedDevicesArrayAdapter;
    private TextView title;
    private TextView tvDeviceListNewDeviceTitle;
    private TextView tvDeviceListPairedDeviceTitle;

    class Device241 implements OnClickListener {
        Device241() {
        }

        public void onClick(View v) {
            DeviceListActivity.this.scanDevices();
            DeviceListActivity.this.btnDeviceListScan.setVisibility(View.GONE);
        }
    }

    class Device252 implements OnItemClickListener {
        Device252() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3) {
            DeviceListActivity.this.bluetoothAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent intent = new Intent();
            intent.putExtra(DeviceListActivity.DEVICE_ADDRESS, address);
            DeviceListActivity.this.setResult(-1, intent);
            DeviceListActivity.this.finish();
        }
    }

    class Device263 implements OnItemClickListener {
        Device263() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3) {
            DeviceListActivity.this.bluetoothAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            new Intent().putExtra(DeviceListActivity.DEVICE_ADDRESS, address);
            DeviceListActivity.this.pairDevice(DeviceListActivity.this.bluetoothAdapter.getRemoteDevice(address));
        }
    }

    class Device274 extends BroadcastReceiver {
        Device274() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(intent.getAction())) {
                int state = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
                int prevState = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", Integer.MIN_VALUE);
                if (state == 12 && prevState == 11) {
                    DeviceListActivity.this.sendActivityResult(((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress());
                } else if (state != 10 || prevState != 12) {
                }
            }
        }
    }

    class Device285 extends BroadcastReceiver {
        Device285() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (device.getBondState() != 12) {
                    DeviceListActivity.this.newDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                DeviceListActivity.this.title.setText(R.string.select_device);
                if (DeviceListActivity.this.newDevicesArrayAdapter.getCount() == 0) {
                    DeviceListActivity.this.newDevicesArrayAdapter.add(DeviceListActivity.this.getResources().getText(R.string.none_found).toString());
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(5);
        setContentView(R.layout.device_list);
        setResult(0);
        getWidgetReferences();
        bindEventHandler();
        initializeValues();
    }

    private void getWidgetReferences() {
        this.title = (TextView) findViewById(R.id.title);
        this.tvDeviceListPairedDeviceTitle = (TextView) findViewById(R.id.tvDeviceListPairedDeviceTitle);
        this.tvDeviceListNewDeviceTitle = (TextView) findViewById(R.id.tvDeviceListNewDeviceTitle);
        this.lvDeviceListPairedDevice = (ListView) findViewById(R.id.lvDeviceListPairedDevice);
        this.lvDeviceListNewDevice = (ListView) findViewById(R.id.lvDeviceListNewDevice);
        this.btnDeviceListScan = (Button) findViewById(R.id.btnDeviceListScan);
    }

    private void bindEventHandler() {
        this.lvDeviceListPairedDevice.setOnItemClickListener(this.mDeviceClickListener);
        this.lvDeviceListNewDevice.setOnItemClickListener(this.mNewDeviceClickListener);
        this.btnDeviceListScan.setOnClickListener(new Device241());
    }

    private void initializeValues() {
        this.pairedDevicesArrayAdapter = new ArrayAdapter(this, R.layout.device_name);
        this.newDevicesArrayAdapter = new ArrayAdapter(this, R.layout.device_name);
        this.lvDeviceListPairedDevice.setAdapter(this.pairedDevicesArrayAdapter);
        this.lvDeviceListNewDevice.setAdapter(this.newDevicesArrayAdapter);
        registerReceiver(this.discoveryFinishReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
        registerReceiver(this.discoveryFinishReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        registerReceiver(this.mPairReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            this.tvDeviceListPairedDeviceTitle.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                this.pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            return;
        }
        this.pairedDevicesArrayAdapter.add(getResources().getText(R.string.none_paired).toString());
    }

    private void scanDevices() {
        if (VERSION.SDK_INT >= 23) {
            switch (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.ACCESS_COARSE_LOCATION")) {
                case -1:
                    if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 1);
                        return;
                    }
                    return;
                case 0:
                    startDiscovery(true);
                    return;
                default:
                    return;
            }
        }
    }

    public void startDiscovery(boolean isAllow) {
        if (isAllow) {
            this.title.setText(R.string.scanning);
            this.tvDeviceListNewDeviceTitle.setVisibility(View.VISIBLE);
            if (this.bluetoothAdapter.isDiscovering()) {
                this.bluetoothAdapter.cancelDiscovery();
            }
            this.bluetoothAdapter.startDiscovery();
            return;
        }
        this.btnDeviceListScan.setVisibility(View.VISIBLE);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == 0) {
                    startDiscovery(true);
                    return;
                } else {
                    startDiscovery(false);
                    return;
                }
            default:
                return;
        }
    }

    public void sendActivityResult(String address) {
        Intent intent1 = new Intent();
        intent1.putExtra(DEVICE_ADDRESS, address);
        setResult(-1, intent1);
        finish();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            device.getClass().getMethod("createBond", (Class[]) null).invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.bluetoothAdapter != null) {
            this.bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(this.discoveryFinishReceiver);
        unregisterReceiver(this.mPairReceiver);
    }
}
