package com.ziasy.haanbaba.intellishopping.bluetooth.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.vision.barcode.Barcode;
import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.bluetooth.BlueToothMainActivity;
import com.ziasy.haanbaba.intellishopping.bluetooth.WebViewJavaScriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.androidhive.barcode.BarcodeReader.BarcodeReaderListener;


public class TransactionActivity extends AppCompatActivity implements BarcodeReaderListener {
    TransactionActivity activity;
    View barcode_scanner;
    String device_address = "";
    String device_id = "";
    WebView main_webview;
    EditText text_destination_add;
    EditText text_sender_add;
    EditText text_value;
    WebViewJavaScriptInterface webViewJavaScriptInterface;

    class TransClick implements OnClickListener {

        class TransClickInside implements Runnable {
            TransClickInside() {
            }

            public void run() {
                TransactionActivity.this.barcode_scanner.setVisibility(View.VISIBLE);
            }
        }

        TransClick() {
        }

        public void onClick(View view) {
            TransactionActivity.this.activity.runOnUiThread(new TransClickInside());
        }
    }

    class C03422 implements OnClickListener {
        C03422() {
        }

        public void onClick(View view) {
            TransactionActivity.this.activity.createTransaction(TransactionActivity.this.text_sender_add.getText().toString(), TransactionActivity.this.text_destination_add.getText().toString(), TransactionActivity.this.getString(R.string.ripple_secret), TransactionActivity.this.text_value.getText().toString());
        }
    }

    class TranSa implements WebViewJavaScriptInterface.WebViewResponse {

        class TranSaInside implements Runnable {
            TranSaInside() {
            }

            public void run() {
                try {
                    BlueToothMainActivity.activity.sendMessage(new JSONObject().put("state", 1).toString());
                    TransactionActivity.this.setResult(-1);
                    TransactionActivity.this.finish();
                } catch (JSONException e) {
                    Toast.makeText(TransactionActivity.this.activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    TransactionActivity.this.setResult(0);
                    TransactionActivity.this.finish();
                    e.printStackTrace();
                }
            }
        }

        TranSa() {
        }

        public void onSuccess(String result) {
            TransactionActivity.this.activity.runOnUiThread(new TranSaInside());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(String.format(getString(R.string.balance), new Object[]{BlueToothMainActivity.activity.myBalance}));
        setContentView((int) R.layout.activity_transaction);
        this.activity = this;
        Bundle data = getIntent().getExtras();
        if (data != null) {
            this.device_id = data.getString("device_id");
            this.device_address = data.getString("device_address");
        }
        this.barcode_scanner = findViewById(R.id.barcode_scanner);
        this.barcode_scanner.setVisibility(View.GONE);
        this.main_webview = (WebView) findViewById(R.id.main_webview);
        this.main_webview.setVisibility(View.GONE);
        this.main_webview.setWebViewClient(new WebViewClient());
        this.main_webview.getSettings().setJavaScriptEnabled(true);
        String file = "file:///android_asset/" + "index.html";
        this.main_webview.getSettings().setJavaScriptEnabled(true);
        this.webViewJavaScriptInterface = new WebViewJavaScriptInterface(this);
        this.main_webview.addJavascriptInterface(this.webViewJavaScriptInterface, "Android");
        this.main_webview.loadUrl(file);
        this.text_sender_add = (EditText) findViewById(R.id.text_sender_add);
        this.text_sender_add.setText(getString(R.string.ripple_Address));
        ((ImageButton) findViewById(R.id.btn_scan)).setOnClickListener(new TransClick());
        this.text_destination_add = (EditText) findViewById(R.id.text_destination_add);
        this.text_destination_add.setText(this.device_address);
        ((TextView) findViewById(R.id.form_title)).setText(String.format(getString(R.string.create_transaction_for), new Object[]{this.device_address}).toString());
        this.text_value = (EditText) findViewById(R.id.text_value);
        ((Button) findViewById(R.id.btn_send)).setOnClickListener(new C03422());
    }

    public void createTransaction(String sAddress, String dAddress, String secret, String amount) {
        this.main_webview.loadUrl("javascript:getTransfer('" + sAddress + "','" + dAddress + "','" + secret + "','" + amount + "')");
        this.webViewJavaScriptInterface.callJavascriptfunc(new TranSa());
    }

    public void onScanned(final Barcode barcode) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                if (barcode != null) {
                    TransactionActivity.this.text_destination_add.setText(barcode.displayValue);
                }
                TransactionActivity.this.barcode_scanner.setVisibility(View.GONE);
            }
        });
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
