package com.ziasy.haanbaba.intellishopping.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.ziasy.haanbaba.intellishopping.Adapter.PrelistDatabaseAdapter;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.RecyclerItemTouchHelper;
import com.ziasy.haanbaba.intellishopping.Common.onCallBackInterfaceForTest;
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

public class PreList_Fragment extends Fragment implements View.OnClickListener,RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private RecyclerView recyclerView;
    private List<ProductDatabaseModel> list;
    private List<ProductDataModel.ProductData> productdataList;
    private ArrayList<String> spinnerList;
    private PrelistDatabaseAdapter prelistAdapter;
    private FloatingActionButton fab;
    private EditText edtQuantity;
    private TextView txtAdd, txtCancel;
 //   private ImageView imageMinus,imageAdd;
    private Dialog dialog;
    private SearchableSpinner spinnerId;
    private ConnectionDetector cd;
   // private int count=0;
    private String oldQuantity;
    private ApiInterface apiService;
    private String productName, productId, productImage, productWeigth, productPrice, productBarcode,productRetailer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prelist, null, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewId);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        dialog = new Dialog(getContext());
        cd=new ConnectionDetector(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_item);
        spinnerId = (SearchableSpinner) dialog.findViewById(R.id.spinnerId);
        edtQuantity = (EditText) dialog.findViewById(R.id.edtQuantity);
        txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        txtAdd = (TextView) dialog.findViewById(R.id.txtAdd);
        //imageAdd = (ImageView) dialog.findViewById(R.id.imageAdd);
        //imageMinus = (ImageView) dialog.findViewById(R.id.imageMinus);

        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
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
            getProductdata(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        list = DBUtil.fetchAllDay(getActivity());
        prelistAdapter = new PrelistDatabaseAdapter(getActivity(), list);
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
        });
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        fab.setOnClickListener(this);
        txtAdd.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        //imageMinus.setOnClickListener(this);
       // imageAdd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        list = DBUtil.fetchAllDay(getActivity());
        prelistAdapter = new PrelistDatabaseAdapter(getActivity(), list);
        recyclerView.setAdapter(prelistAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                dialog.show();
                break;


            case R.id.txtAdd:
                String strQuantity = edtQuantity.getText().toString().trim();
                if (strQuantity.isEmpty()) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Enter Quantity", Snackbar.LENGTH_SHORT).show();
                    edtQuantity.setError("Please Enter Quantity");

                } else  if (strQuantity.length()<0) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Qauntity is zero", Snackbar.LENGTH_SHORT).show();
                    edtQuantity.setError("Qauntity is zero");


                } else  if (productId==null) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Select Product", Snackbar.LENGTH_SHORT).show();

                } else {
                    edtQuantity.setText("");

                    if (!DBUtil.checkProduct(getContext(), productId)) {
                        ProductDatabaseModel newExercise =  DBUtil.productInsert(getContext(), Integer.parseInt(productId), productName, strQuantity, productWeigth, productImage, productPrice,productRetailer);
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
                        DBUtil.updateProduct(getContext(), new ProductDatabaseModel(1, Integer.parseInt(productId), productName, String.valueOf(totalQuantity), productWeigth, productImage, productPrice,productRetailer));
                        dialog.dismiss();

                        if (prelistAdapter != null) {
                            prelistAdapter.notifyDataSetChanged();

                        }
                    }
                }
                break;
            case R.id.txtCancel:

                dialog.dismiss();
                break;
        }
    }

    private void getProductdata(JsonObject object) {
    if (!cd.isConnectingToInternet()){
        Snackbar.make(getActivity().findViewById(android.R.id.content),R.string.NoConnection,Snackbar.LENGTH_SHORT).show();
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerList);
                    spinnerId.setAdapter(adapter);

                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Invalid Credential", Snackbar.LENGTH_SHORT).show();

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
                    .make(getActivity().findViewById(android.R.id.content), name + " removed from cart!", Snackbar.LENGTH_LONG);
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
    }
}