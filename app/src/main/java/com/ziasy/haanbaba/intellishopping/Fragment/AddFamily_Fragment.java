package com.ziasy.haanbaba.intellishopping.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.ziasy.haanbaba.intellishopping.Activity.NotificationActivity;
import com.ziasy.haanbaba.intellishopping.Adapter.PrelistAdapter;
import com.ziasy.haanbaba.intellishopping.Adapter.familyAdapter;
import com.ziasy.haanbaba.intellishopping.Adapter.notificationAdapter;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.Model.PrelistModel;
import com.ziasy.haanbaba.intellishopping.Model.addFamilyModel;
import com.ziasy.haanbaba.intellishopping.Model.familyModel;
import com.ziasy.haanbaba.intellishopping.Model.notificationModel;
import com.ziasy.haanbaba.intellishopping.Network.ApiClient;
import com.ziasy.haanbaba.intellishopping.Network.ApiInterface;
import com.ziasy.haanbaba.intellishopping.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFamily_Fragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerViewId;
    private List<familyModel.familyDataList> dataList;
    private familyAdapter adapter;
    private FloatingActionButton fab;
    private Dialog dialog;
    private EditText edtMobile, edtName;
    private TextView txtAddItem;
    private String strMobile, strName;
    private ConnectionDetector cd;
    private SessionManagement sd;
    private ApiInterface apiService;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prelist, null, false);
        cd = new ConnectionDetector(getContext());
        sd = new SessionManagement(getContext());
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        recyclerViewId = (RecyclerView) view.findViewById(R.id.recyclerViewId);
        dataList = new ArrayList<>();
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_familly);
        edtName = (EditText) dialog.findViewById(R.id.edtName);
        edtMobile = (EditText) dialog.findViewById(R.id.edtMobile);
        txtAddItem = (TextView) dialog.findViewById(R.id.txtAddItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewId.setLayoutManager(linearLayoutManager);

        try {
            JsonObject object = new JsonObject();
            object.addProperty("id", sd.getUserId());
            getData(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new familyAdapter(getContext(), dataList);
        recyclerViewId.setAdapter(adapter);
        fab.setOnClickListener(this);
        txtAddItem.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                dialog.show();
                break;

            case R.id.txtAddItem:
                strMobile = edtMobile.getText().toString().trim();
                strName = edtName.getText().toString().trim();
                if (strName.isEmpty()) {
                    edtName.setError("Please Enter Name");
                } else if (strMobile.isEmpty()) {
                    edtMobile.setError("Please Enter Number");
                } else if (strMobile.length() != 10) {
                    edtMobile.setError("Enter 10 Digit Number");
                } else {
                    try {
                        JsonObject object = new JsonObject();
                        object.addProperty("id", sd.getUserId());
                        object.addProperty("name", strName);
                        object.addProperty("mobile", strMobile);
                        object.addProperty("gender", "Male");
                        object.addProperty("relation", "Brother");
                        addFamilyMember(object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

            default:
                break;
        }
    }

    private void addFamilyMember(final JsonObject object) {
        if (!cd.isConnectingToInternet()) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();
        } else {
            try {
                progressDialog.show();
                Call<addFamilyModel> call = apiService.addFamilyMember(object);
                call.enqueue(new Callback<addFamilyModel>() {
                    @Override
                    public void onResponse(Call<addFamilyModel> call, Response<addFamilyModel> response) {
                        dialog.dismiss();
                        edtName.setText("");
                        edtMobile.setText("");
                        if (response.body().code.equalsIgnoreCase("200")) {
                            progressDialog.dismiss();
                            Log.e("OBJECT_DATA", object.toString());
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Add Succesfully", Snackbar.LENGTH_SHORT).show();
                            familyModel.familyDataList model = new familyModel.familyDataList();
                            try {
                                JSONObject jsonObject = new JSONObject(object.toString());
                                model.setMobile(jsonObject.optString("mobile").toString().trim());
                                model.setName(jsonObject.optString("name").toString().trim());
                                dataList.add(model);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (response.body().response.equalsIgnoreCase("Maximum Limit Over")) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Maximum Limit Over", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Invalid Credential", Snackbar.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<addFamilyModel> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("AddFamily_Fragment", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                dialog.dismiss();
            }
        }
    }

    private void getData(JsonObject object) {
        if (!cd.isConnectingToInternet()) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();
        } else {
            try {
                progressDialog.show();
                Call<familyModel> call = apiService.getFamilyList(object);
                progressDialog.dismiss();
                call.enqueue(new Callback<familyModel>() {
                    @Override
                    public void onResponse(Call<familyModel> call, Response<familyModel> response) {
                        progressDialog.dismiss();
                        if (response.body().code.equalsIgnoreCase("200")) {

                            dataList = response.body().dataList;
                            adapter = new familyAdapter(getContext(), dataList);
                            recyclerViewId.setAdapter(adapter);
                        } else {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Invalid Credential", Snackbar.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<familyModel> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("AddFamily_Fragment", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();

            }
        }
    }
}
