package com.ziasy.haanbaba.intellishopping.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.google.gson.JsonObject;
import com.ziasy.haanbaba.intellishopping.Adapter.notificationAdapter;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.Model.ProductDataModel;
import com.ziasy.haanbaba.intellishopping.Model.notificationModel;
import com.ziasy.haanbaba.intellishopping.Network.ApiClient;
import com.ziasy.haanbaba.intellishopping.Network.ApiInterface;
import com.ziasy.haanbaba.intellishopping.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerViewId;

    private notificationAdapter adapter;
    private ConnectionDetector cd;
    private SessionManagement sd;
    private ApiInterface apiService;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_orderlist);
        cd=new ConnectionDetector(NotificationActivity.this);
        sd=new SessionManagement(NotificationActivity.this);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressDialog=new ProgressDialog(NotificationActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
/*     getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  */

        recyclerViewId = (RecyclerView) findViewById(R.id.recyclerViewId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        recyclerViewId.setLayoutManager(linearLayoutManager);

        try {
            JsonObject object = new JsonObject();
            object.addProperty("notification", "notification");
            getData(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    private void getData(JsonObject object){
        if (!cd.isConnectingToInternet()){
            Snackbar.make(findViewById(android.R.id.content),R.string.NoConnection,Snackbar.LENGTH_SHORT).show();
        }else {
            try {


            progressDialog.show();
            Call<notificationModel> call = apiService.getNotificationList(object);
            call.enqueue(new Callback<notificationModel>() {
                @Override
                public void onResponse(Call<notificationModel> call, Response<notificationModel> response) {
                    progressDialog.dismiss();
                    if (response.body().code.equalsIgnoreCase("200")) {
                        adapter=new notificationAdapter(NotificationActivity.this,response.body().dataList);
                        recyclerViewId.setAdapter(adapter);


                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Invalid Credential", Snackbar.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<notificationModel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("SignUpActivity", t.toString());
                    progressDialog.dismiss();
                }
            });
            }catch (Exception e){
                e.printStackTrace();
                progressDialog.dismiss();
            }
        }

    }
}
