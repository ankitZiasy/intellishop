package com.ziasy.haanbaba.intellishopping.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.ziasy.haanbaba.intellishopping.Activity.CheckOutActivity;
import com.ziasy.haanbaba.intellishopping.Activity.MainActivity;
import com.ziasy.haanbaba.intellishopping.Activity.ScanBarcodeActivity;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.DB.DBUtil;
import com.ziasy.haanbaba.intellishopping.DB.ScanProductDatabaseModel;
import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.bluetooth.activity.ScanDeviceActivity;
import com.ziasy.haanbaba.intellishopping.bluetooth.activity.TestModeActivity;
import com.ziasy.haanbaba.intellishopping.bluetooth.activity.TransactionActivity;
import com.ziasy.haanbaba.intellishopping.bluetooth.adapter.DeviceListAdapter;
import com.ziasy.haanbaba.intellishopping.bluetooth.fragments.Fragment_Signal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BlueToothMainActivity extends AppCompatActivity {
    public static final int DEVICE_BT_CONNECTED = 1;
    public static final String DEVICE_NAME = "device_name";
    public static final int DEVICE_NOT_CONNECTED = 0;
    public static final int DEVICE_SCANNED = 2;
    public static int DEVICE_STATUS = 0;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_WRITE = 3;
    private static final int REQUEST_CONFIGURE_WALLET = 5;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int REQUEST_SCAN_DEVICE = 4;
    private static final int REQUEST_TRANSACTION = 6;
    public static final int STATUS_FAILED = 3;
    public static final int STATUS_INIT = 0;
    public static final int STATUS_PENDING = 2;
    public static final int STATUS_SUCCESS = 1;
    public static final String TOAST = "toast";
    private SessionManagement sd;
    private String qauntity,count;
    private Button btn_connect;
    String bluetoothWiegth,bluetoothCount,bluetoothPid;
    private List<ScanProductDatabaseModel> list;
    public static BlueToothMainActivity activity;
    BluetoothAdapter BTAdapter;
    public int REQUEST_BLUETOOTH = 1000;
    //FloatingActionButton btn_connect;
    private ChatService chatService = null;
    public String connectedDeviceName = null;
    ArrayList<Device> connectedDevicesArrayAdapter = new ArrayList();
    FrameLayout content;
    Device deviceConnected;
    DeviceListAdapter deviceListAdapter;
    private Handler handler = new Handler(new MainActiviytCallBack());
    LinearLayout layout_list_connected;
    RelativeLayout layout_signal_wait;
    ListView lvDeviceListConnected;
    private OnItemClickListener mDeviceClickListener = new MainActiviytClickListner57();
    private final BroadcastReceiver mReceiver = new MainActiviytClickListner38();
    WebView main_webview;
    public String myBalance = "";
    private StringBuffer outStringBuffer;
    public boolean testMODE = true;
    WebViewJavaScriptInterface webViewJavaScriptInterface;

    class MainActiviytCallBack implements Callback {
        MainActiviytCallBack() {
        }

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    switch (msg.arg1) {
                        case 0:
                        case 1:
                            BlueToothMainActivity.this.setStatus(BlueToothMainActivity.this.getString(R.string.title_not_connected));
                            break;
                        case 2:
                            BlueToothMainActivity.this.setStatus(BlueToothMainActivity.this.getString(R.string.title_connecting));
                            break;
                        case 3:
                            BlueToothMainActivity.this.setStatus(BlueToothMainActivity.this.getString(R.string.title_connected_to, new Object[]{BlueToothMainActivity.this.connectedDeviceName}));
                            if (!BlueToothMainActivity.this.testMODE) {
                                BlueToothMainActivity.this.showScanDeviceActivity();
                                break;
                            }
                            BlueToothMainActivity.this.showTestModeActivity();
                            break;
                        default:
                            break;
                    }
                case 2:
                    try {
                        BlueToothMainActivity.this.parseResponse(new String((byte[]) msg.obj, 0, msg.arg1));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    String writeMessage = new String((byte[]) msg.obj);
                    break;
                case 4:
                    BlueToothMainActivity.this.connectedDeviceName = msg.getData().getString(BlueToothMainActivity.DEVICE_NAME);
                    Toast.makeText(BlueToothMainActivity.this.getApplicationContext(), "Connected to " + BlueToothMainActivity.this.connectedDeviceName, Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(BlueToothMainActivity.this.getApplicationContext(), msg.getData().getString(BlueToothMainActivity.TOAST), Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    }

    class MainActiviytClickListner implements OnClickListener {
        MainActiviytClickListner() {
        }

        public void onClick(View view) {
            BlueToothMainActivity.this.startActivityForResult(new Intent(BlueToothMainActivity.activity, DeviceListActivity.class), 2);
        }
    }

    class MainActiviytClickListner12 implements OnClickListener {
        MainActiviytClickListner12() {
        }

        public void onClick(View view) {
            BlueToothMainActivity.this.sendMessage("A");
        }
    }

    class MainActiviytClickListner35 implements OnClickListener {
        MainActiviytClickListner35() {
        }

        public void onClick(View view) {
            BlueToothMainActivity.this.sendMessage("B");
        }
    }

    class MainActiviytClickListner57 implements OnItemClickListener {
        MainActiviytClickListner57() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3) {
            Toast.makeText(BlueToothMainActivity.this, "Please Tap on device to initiate!", Toast.LENGTH_LONG).show();
            BlueToothMainActivity.this.showTransactionFragment(BlueToothMainActivity.this.deviceConnected.id, BlueToothMainActivity.this.deviceConnected.address);
        }
    }

    class MainActiviytClickListner38 extends BroadcastReceiver {
        MainActiviytClickListner38() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (device.getBondState() != 12) {
                    Toast.makeText(context, device.getName() + "\n" + device.getAddress(), Toast.LENGTH_LONG).show();
                }
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                Toast.makeText(context, "No Device", Toast.LENGTH_LONG).show();
            }
        }
    }

    class MainActivityResponseListener implements WebViewJavaScriptInterface.WebViewResponse {
        MainActivityResponseListener() {
        }

        public void onSuccess(final String result) {
            BlueToothMainActivity.activity.runOnUiThread(new Runnable() {
                public void run() {
                    BlueToothMainActivity.this.myBalance = result;
                    BlueToothMainActivity.this.setTitle(String.format(BlueToothMainActivity.this.getString(R.string.balance), new Object[]{result}));
                }
            });
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.bluetooth_activity_main);
        activity = this;
        this.content = (FrameLayout) findViewById(R.id.content);
        this.content.setVisibility(View.GONE);
        initListView();
        this.btn_connect = (Button) findViewById(R.id.btn_connect);
        this.btn_connect.setOnClickListener(new MainActiviytClickListner());
        this.main_webview = (WebView) findViewById(R.id.main_webview);
        this.main_webview.setVisibility(View.GONE);
        this.main_webview.getSettings().setJavaScriptEnabled(true);
        final String file = "file:///android_asset/" + "index.html";
        this.main_webview.getSettings().setJavaScriptEnabled(true);
        this.webViewJavaScriptInterface = new WebViewJavaScriptInterface(this);
        this.main_webview.addJavascriptInterface(this.webViewJavaScriptInterface, "Android");
        this.main_webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (url.equals(file)) {
                    BlueToothMainActivity.this.getBalance();
                }
            }
        });
        this.main_webview.loadUrl(file);
        initBT();
        list = new ArrayList<>();
        sd=new SessionManagement(BlueToothMainActivity.this);
        list = DBUtil.scanfetchAllDay(BlueToothMainActivity.this);
        showTotal(list);
    }
    public void showTotal(List<ScanProductDatabaseModel> list) {
        Double total = 00.0;

        int qountity = 0 ;Double weight = 00.0;
        if (list!=null){
            for (int i = 0; i < list.size(); i++) {
                weight = weight + Double.parseDouble(list.get(i).getProductWeigth());
                qountity = Integer.parseInt(qountity + list.get(i).getProductQuantity());
                total = total + Double.valueOf(list.get(i).getProductWeigth());
            }
            count= String.valueOf(qountity);
            qauntity= String.valueOf(weight);
        }
    }

    public void initListView() {
        this.layout_list_connected = (LinearLayout) findViewById(R.id.layout_list_connected);
        this.lvDeviceListConnected = (ListView) findViewById(R.id.lvDeviceListConnected);
        this.layout_signal_wait = (RelativeLayout) findViewById(R.id.layout_signal_wait);
        this.layout_signal_wait.setVisibility(View.GONE);
        ((Button) findViewById(R.id.btn_on)).setOnClickListener(new MainActiviytClickListner12());
        ((Button) findViewById(R.id.btn_off)).setOnClickListener(new MainActiviytClickListner35());
    }

    public void getBalance() {
        this.main_webview.loadUrl("javascript:getBalance('" + getString(R.string.ripple_Address) + ")')");
        this.webViewJavaScriptInterface.callJavascriptfunc(new MainActivityResponseListener());
    }

    public void initBT() {
        this.BTAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void parseResponse(String response) {
        Toast.makeText(activity, response, Toast.LENGTH_LONG).show();
        Log.e("response",response);
        try {
            JSONObject object=new JSONObject(response);
            bluetoothWiegth=object.getString("weight");
           // bluetoothCount=object.getString("count");
           // bluetoothPid=object.getString("pid");
            Double serverWeight= Double.valueOf(bluetoothWiegth);
            Double plusWeigth= Double.valueOf(qauntity)+4.0;
            Double plusMinus= Double.valueOf(qauntity)-4.0;
            if (plusMinus<=serverWeight && serverWeight<= plusWeigth){
          //  if (bluetoothWiegth.equalsIgnoreCase(qauntity)){
                sendMessage("1");
                DBUtil.getScanAllDataForVolley(BlueToothMainActivity.this, sd.getUserTrolleyId());

            }else {
                Toast.makeText(BlueToothMainActivity.this, "Trolley Weigth is not equal", Toast.LENGTH_SHORT).show();

                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (new JSONObject(response).getInt("state") == 0 && DEVICE_STATUS == 2 && this.deviceConnected != null) {
                showTransactionFragment(this.deviceConnected.id, this.deviceConnected.address);
                sendMessage(new JSONObject().put("state", 2).toString());
                return;
            }
            Toast.makeText(activity, "Please Scan QR first.", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showScanDeviceActivity() {
        DEVICE_STATUS = 1;
        startActivityForResult(new Intent(this, ScanDeviceActivity.class), 4);
    }

    public void showTestModeActivity() {
        DEVICE_STATUS = 1;
    sendMessage("0");

      // startActivity(new Intent(this, TestModeActivity.class));
    }

    public void showConnectedDeviceList(String device_info) {
        try {
            DEVICE_STATUS = 2;
            JSONObject obj = new JSONObject(device_info);
            this.deviceConnected = new Device(obj.getString(DEVICE_NAME), obj.getString("device_id"), obj.getString("ripple_address"));
            this.connectedDevicesArrayAdapter.add(this.deviceConnected);
            this.deviceListAdapter = new DeviceListAdapter(BlueToothMainActivity.this, this.connectedDevicesArrayAdapter);
            this.lvDeviceListConnected.setAdapter(this.deviceListAdapter);
            this.lvDeviceListConnected.setOnItemClickListener(this.mDeviceClickListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* public void showConfigureWalletActivity() {
        startActivityForResult(new Intent(this, ConfigureWalletActivity.class), 5);
    }
*/
    public void showTransactionFragment(String device_id, String device_address) {
        Intent serverIntent = new Intent(this, TransactionActivity.class);
        serverIntent.putExtra("device_id", device_id);
        serverIntent.putExtra("device_address", device_address);
        startActivityForResult(serverIntent, 6);
    }

    public void showFragmentSignal() {
        this.content.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add((int) R.id.content, Fragment_Signal.newInstance(activity, "4"));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    connectDevice(data, true);
                    return;
                }
                return;
            case 2:
                if (resultCode == -1) {
                    connectDevice(data, false);
                    return;
                }
                return;
            case 3:
                if (resultCode == -1) {
                    setupChat();
                    return;
                }
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_LONG).show();
                finish();
                return;
            case 4:
                if (resultCode == -1 && data != null) {
                    showConnectedDeviceList(data.getExtras().getString("device_info").toString());
                    return;
                }
                return;
            case 5:
                if (resultCode != -1) {
                    return;
                }
                return;
            case 6:
                if (resultCode != -1) {
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        this.chatService.connect(this.BTAdapter.getRemoteDevice(data.getExtras().getString(DeviceListActivity.DEVICE_ADDRESS)), secure);
    }

   
    public void sendMessage(String message) {
        if (this.chatService.getState() != 3) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
        } else if (message.length() > 0) {
            this.chatService.write(message.getBytes());
            this.outStringBuffer.setLength(0);
        }
    }

    private final void setStatus(CharSequence subTitle) {
        getSupportActionBar().setSubtitle(subTitle);
    }

    private void setupChat() {
        this.chatService = new ChatService(this, this.handler);
        this.outStringBuffer = new StringBuffer("");
    }

    public void onStart() {
        super.onStart();
        if (!this.BTAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 3);
        } else if (this.chatService == null) {
            setupChat();
        }
    }

    public synchronized void onResume() {
        super.onResume();
        if (this.chatService != null && this.chatService.getState() == 0) {
            this.chatService.start();
        }
    }

    public synchronized void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.chatService != null) {
            this.chatService.stop();
        }
    }
}
