package com.ziasy.haanbaba.intellishopping.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.ziasy.haanbaba.intellishopping.Adapter.PrelistDatabaseAdapter;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.RecyclerItemTouchHelper;
import com.ziasy.haanbaba.intellishopping.DB.DBUtil;
import com.ziasy.haanbaba.intellishopping.DB.ProductDatabaseModel;
import com.ziasy.haanbaba.intellishopping.Model.ProductDataModel;
import com.ziasy.haanbaba.intellishopping.Network.ApiClient;
import com.ziasy.haanbaba.intellishopping.Network.ApiInterface;
import com.ziasy.haanbaba.intellishopping.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreList_Activity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<ProductDatabaseModel> list;
    private List<ProductDataModel.ProductData> productdataList;
    private ArrayList<String> spinnerList;
    private PrelistDatabaseAdapter prelistAdapter;
    private FloatingActionButton fab;
    private TextView edtQuantity;
    private TextView txtAdd, txtCancel;
    private ImageView imageMinus,imageAdd;
    private Dialog dialog;
    private SearchableSpinner spinnerId;
    private ConnectionDetector cd;
    private int count=1;
    private String oldQuantity;
    private ApiInterface apiService;
    private String productName, productId, productImage, productWeigth, productPrice, productBarcode,productRetailer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_prelist);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        dialog = new Dialog(PreList_Activity.this);
        cd=new ConnectionDetector(PreList_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_item);
        spinnerId = (SearchableSpinner) dialog.findViewById(R.id.spinnerId);
        edtQuantity = (TextView) dialog.findViewById(R.id.edtQuantity);
        txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtAdd = (TextView) dialog.findViewById(R.id.txtAdd);
        imageAdd = (ImageView) dialog.findViewById(R.id.imageAdd);
        imageMinus = (ImageView) dialog.findViewById(R.id.imageMinus);

        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PreList_Activity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Use above layout manager for RecyclerView..
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        spinnerList = new ArrayList<>();
        productdataList = new ArrayList<>();
        list = new ArrayList<>();
        try {
            JsonObject object = new JsonObject();
            object.addProperty("method", "product");
            //getProductdata(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        list = DBUtil.fetchAllDay(PreList_Activity.this);
        prelistAdapter = new PrelistDatabaseAdapter(PreList_Activity.this, list);
        recyclerView.setAdapter(prelistAdapter);
        spinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productName = productdataList.get(position).name;
                productId = productdataList.get(position).id;
                productImage = productdataList.get(position).image;
                productWeigth = productdataList.get(position).weight;
                productPrice = productdataList.get(position).amount;
                productRetailer = productdataList.get(position).retailer_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });/*
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);*/

        fab.setOnClickListener(this);
        txtAdd.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        imageMinus.setOnClickListener(this);
        imageAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                dialog.show();
                break;

          case R.id.imageAdd:
              if(count<100) {
                  count++;
                  edtQuantity.setText(String.valueOf(count));
              }else {
                  Snackbar.make(findViewById(android.R.id.content), "Maximum Limit is 100", Snackbar.LENGTH_SHORT).show();

              }
                break;

          case R.id.imageMinus:
              if(count>0) {
                  count--;
                  edtQuantity.setText(String.valueOf(count));

              }else {
                  Snackbar.make(findViewById(android.R.id.content), "Value not below the Zero", Snackbar.LENGTH_SHORT).show();

              }
                break;

            case R.id.txtAdd:
                String strQuantity = edtQuantity.getText().toString().trim();
                if (strQuantity.isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please Enter Quantity", Snackbar.LENGTH_SHORT).show();


                } else   if (strQuantity.equalsIgnoreCase("0")) {
                    Snackbar.make(findViewById(android.R.id.content), "Your Quantity is 0 ", Snackbar.LENGTH_SHORT).show();


                } else  if (productId==null) {
                    Snackbar.make(findViewById(android.R.id.content), "Select Product", Snackbar.LENGTH_SHORT).show();

                } else {
                    edtQuantity.setText("0");
                    count=0;
                    if (!DBUtil.checkProduct(PreList_Activity.this, productId)) {
                        ProductDatabaseModel newExercise =  DBUtil.productInsert(PreList_Activity.this, Integer.parseInt(productId), productName, strQuantity, productWeigth, productImage, productPrice,productRetailer);
                        list.add(newExercise);
                        prelistAdapter.notifyDataSetChanged();
                        dialog.dismiss();

                    } else {
                             for (ProductDatabaseModel model : list) {
                            if (model.getProductId()==Integer.parseInt(productId)) {
                                oldQuantity=model.getProductQuantity();
                                int totalQuantity=Integer.parseInt(oldQuantity)+Integer.parseInt(strQuantity);
                                model.setProductQuantity(String.valueOf(totalQuantity));
                            }
                        }
                      //  Snackbar.make(getActivity().findViewById(android.R.id.content), "Already Add", Snackbar.LENGTH_SHORT).show();
                             int totalQuantity=Integer.parseInt(oldQuantity)+Integer.parseInt(strQuantity);
                           //  Snackbar.make(getActivity().findViewById(android.R.id.content),"Total Quantity : "+totalQuantity,Snackbar.LENGTH_SHORT).show();
                        DBUtil.updateProduct(PreList_Activity.this, new ProductDatabaseModel(1, Integer.parseInt(productId), productName, String.valueOf(totalQuantity), productWeigth, productImage, productPrice,productRetailer));
                        dialog.dismiss();

                        if (prelistAdapter != null) {
                            prelistAdapter.notifyDataSetChanged();

                        }
                    }
                }
                break;
            case R.id.txtCancel:
                edtQuantity.setText("0");
                count=0;
                dialog.dismiss();
                break;
        }
    }


    /*
    private void getProductdata(JsonObject object) {
    if (!cd.isConnectingToInternet()){
        Snackbar.make(findViewById(android.R.id.content),R.string.NoConnection,Snackbar.LENGTH_SHORT).show();
    }else {
        Call<ProductDataModel> call = apiService.getProductList(object);
        call.enqueue(new Callback<ProductDataModel>() {
            @Override
            public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                if (response.body().code.equalsIgnoreCase("200")) {
                    for (int i = 0; i < response.body().dataList.size(); i++) {
                        spinnerList.add(response.body().dataList.get(i).name);
                    }
                    productdataList.addAll(response.body().dataList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PreList_Activity.this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
                    spinnerId.setAdapter(adapter);

                } else {
                    Snackbar.make(PreList_Activity.this.findViewById(android.R.id.content), "Invalid Credential", Snackbar.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ProductDataModel> call, Throwable t) {
                // Log error here since request failed
                Log.e("SignUpActivity", t.toString());
            }
        });
    }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof PrelistDatabaseAdapter.prelistHolder) {
            // get the removed item name to display it in snack bar
            String name = list.get(viewHolder.getAdapterPosition()).getProductName();

            // backup of removed item for undo purpose
            final ProductDatabaseModel deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            prelistAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    prelistAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }*/
}
