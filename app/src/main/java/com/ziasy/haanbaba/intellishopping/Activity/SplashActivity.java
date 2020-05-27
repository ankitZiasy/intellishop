package com.ziasy.haanbaba.intellishopping.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.R;
import com.ziasy.haanbaba.intellishopping.Utills.BaseActivity;

public class SplashActivity extends BaseActivity {

    private SessionManagement sd;
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        sd=new SessionManagement(SplashActivity.this);
        Log.e("STATUS",sd.getLoginStatus());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sd.getLoginStatus().equalsIgnoreCase("true")){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
    }
}
