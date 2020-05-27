package com.ziasy.haanbaba.intellishopping.Activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.ziasy.haanbaba.intellishopping.Common.Connection;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.Constant;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.DB.DBUtil;
import com.ziasy.haanbaba.intellishopping.DB.ScanProductDatabaseModel;
import com.ziasy.haanbaba.intellishopping.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import static junit.framework.Assert.assertNotNull;

public class ScanTrolleyBarcodeActivity extends AppCompatActivity {
    private   String id,name,qrcode,trolley_id,weight,description,date,status;
    private static final String BARCODE_KEY = "BARCODE";
   private RequestQueue queue;
    private Barcode barcodeResult;
    private SessionManagement sd;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_trolley_activity);
        sd=new SessionManagement(ScanTrolleyBarcodeActivity.this);
        cd=new ConnectionDetector(ScanTrolleyBarcodeActivity.this);


        queue = Volley.newRequestQueue(this);
        startScan();
        if(savedInstanceState != null){
            Barcode restoredBarcode = savedInstanceState.getParcelable(BARCODE_KEY);
            if(restoredBarcode != null){
                barcodeResult = restoredBarcode;

            }
        }
    }

    private void makeJsonObjReq(String barcode) {
        if (!cd.isConnectingToInternet()){
            Snackbar.make(findViewById(android.R.id.content),R.string.NoConnection,Snackbar.LENGTH_SHORT).show();
        }else {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("qrcode", barcode);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constant.SEND_TROLLEY, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //   Log.d(TAG, response.toString());
                        try {
                            String code = response.getString("code");
                            if (code.equalsIgnoreCase("200")){

                                JSONArray array = response.getJSONArray("response");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                     id = object.getString("id");
                                     name = object.getString("name");
                                     qrcode = object.getString("qrcode");
                                     trolley_id = object.getString("trolley_id");
                                     weight = object.getString("weight");
                                     description = object.getString("description");
                                     date = object.getString("date");
                                     status = object.getString("status");
                                    Log.e("COUNTER","CONTER Increase");
                                    DBUtil.getAllDataForVolley(ScanTrolleyBarcodeActivity.this,id);
                                }

                            }else {
                                Toast.makeText(ScanTrolleyBarcodeActivity.this,"Not Available in list",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //  VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(jsonObjReq);
    }
    }
    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(ScanTrolleyBarcodeActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        barcodeResult = barcode;
                        makeJsonObjReq(barcode.rawValue);
                        finish();
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BARCODE_KEY, barcodeResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan();
            return;
        }
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }
}