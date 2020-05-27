package com.ziasy.haanbaba.intellishopping.bluetooth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;


import com.google.android.gms.vision.barcode.Barcode;
import com.ziasy.haanbaba.intellishopping.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;


public class ScanDeviceActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    ScanDeviceActivity activity;
    View barcode_scanner;

    class ScanA implements Runnable {
        ScanA() {
        }

        public void run() {
            ScanDeviceActivity.this.barcode_scanner.setVisibility(View.VISIBLE);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.scan_device);
        setTitle("Scan Device");
        this.activity = this;
        this.barcode_scanner = findViewById(R.id.barcode_scanner);
        this.barcode_scanner.setVisibility(View.GONE);
        new Handler().postDelayed(new ScanA(), 1000);
    }

    public void onScanned(Barcode barcode) {
        String result = barcode.displayValue;
        if (result != null && !result.equals("")) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("device_info", jsonObj.toString());
                setResult(-1, resultIntent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onScannedMultiple(List<Barcode> list) {
    }

    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
    }

    public void onScanError(String errorMessage) {
    }

    public void onCameraPermissionDenied() {
    }
}
