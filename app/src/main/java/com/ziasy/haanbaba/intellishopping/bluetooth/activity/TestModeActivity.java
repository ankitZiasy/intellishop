package com.ziasy.haanbaba.intellishopping.bluetooth.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.bluetooth.BlueToothMainActivity;


public class TestModeActivity extends AppCompatActivity {
    OnClickListener onBtnClickListner = new C03391();

    class C03391 implements OnClickListener {
        C03391() {
        }

        public void onClick(View view) {
            Button btn = (Button) view;
            Toast.makeText(TestModeActivity.this, "SIGNAL: " + Integer.valueOf(btn.getText().toString()).intValue(), Toast.LENGTH_LONG).show();
            BlueToothMainActivity.activity.sendMessage(btn.getText().toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_test_mode);
        ((Button) findViewById(R.id.btn_9)).setOnClickListener(this.onBtnClickListner);
        ((Button) findViewById(R.id.btn_0)).setOnClickListener(this.onBtnClickListner);
        ((Button) findViewById(R.id.btn_1)).setOnClickListener(this.onBtnClickListner);
        ((Button) findViewById(R.id.btn_2)).setOnClickListener(this.onBtnClickListner);
        ((Button) findViewById(R.id.btn_3)).setOnClickListener(this.onBtnClickListner);
        ((Button) findViewById(R.id.btn_4)).setOnClickListener(this.onBtnClickListner);
        ((Button) findViewById(R.id.btn_5)).setOnClickListener(this.onBtnClickListner);
        ((Button) findViewById(R.id.btn_6)).setOnClickListener(this.onBtnClickListner);
    }
}
