package com.ziasy.haanbaba.intellishopping.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.ziasy.haanbaba.intellishopping.Adapter.OfferAdapter;
import com.ziasy.haanbaba.intellishopping.Model.offerModel;
import com.ziasy.haanbaba.intellishopping.R;

import java.util.ArrayList;

public class OfferActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<offerModel> dataList = null;
    OfferAdapter adapter;
    RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_prelist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       /* getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        dataList = new ArrayList<>();
        adapter = new OfferAdapter(dataList, OfferActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    fab.setVisibility(View.GONE);
        addData();
    }

    public void addData() {

        dataList.add(new offerModel(R.drawable.ic_launcher_background, "YOU CAN CHANGE YOUR ACCOUNT BALANCE", "CONGATES YOU HAVE WIN THAT PRICE"));
        dataList.add(new offerModel(R.drawable.ic_launcher_background, "YOU CAN CHANGE YOUR ACCOUNT BALANCE", "CONGATES YOU HAVE WIN THAT PRICE"));
        dataList.add(new offerModel(R.drawable.ic_launcher_background, "YOU CAN CHANGE YOUR ACCOUNT BALANCE", "CONGATES YOU HAVE WIN THAT PRICE"));
        dataList.add(new offerModel(R.drawable.ic_launcher_background, "YOU CAN CHANGE YOUR ACCOUNT BALANCE", "CONGATES YOU HAVE WIN THAT PRICE"));
        dataList.add(new offerModel(R.drawable.ic_launcher_background, "YOU CAN CHANGE YOUR ACCOUNT BALANCE", "CONGATES YOU HAVE WIN THAT PRICE"));
        dataList.add(new offerModel(R.drawable.ic_launcher_background, "YOU CAN CHANGE YOUR ACCOUNT BALANCE", "CONGATES YOU HAVE WIN THAT PRICE"));


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
}