package com.ziasy.haanbaba.intellishopping.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ziasy.haanbaba.intellishopping.Adapter.CustomAdapter;
import com.ziasy.haanbaba.intellishopping.Common.ConnectionDetector;
import com.ziasy.haanbaba.intellishopping.Common.Constant;
import com.ziasy.haanbaba.intellishopping.Common.SessionManagement;
import com.ziasy.haanbaba.intellishopping.Model.ChildInfo;
import com.ziasy.haanbaba.intellishopping.Model.GroupInfo;
import com.ziasy.haanbaba.intellishopping.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class OerderFragment extends Fragment {
    private LinkedHashMap<String, GroupInfo> subjects;
    private ArrayList<GroupInfo> deptList;

    private CustomAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;
    //  private List<OrderHistoryModel>list;

    private ConnectionDetector cd;
    private SessionManagement sd;
    private RequestQueue queue;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_history_list, null, false);
        // Set collapsing tool bar image.

        queue = Volley.newRequestQueue(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...!");
        progressDialog.setCancelable(false);

        cd = new ConnectionDetector(getContext());
        sd = new SessionManagement(getContext());
        subjects = new LinkedHashMap<String, GroupInfo>();
        deptList = new ArrayList<GroupInfo>();
        // add data for displaying in expandable list view
        // loadData();

        //get reference of the ExpandableListView
        simpleExpandableListView = (ExpandableListView) view.findViewById(R.id.simpleExpandableListView);
        // create the adapter by passing your ArrayList data
        makeJsonObjReq();


        //expand all the Groups
//        expandAll();

        // setOnChildClickListener listener for child row click
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                GroupInfo headerInfo = deptList.get(groupPosition);
                //get the child info
                ChildInfo detailInfo = headerInfo.getProductList().get(childPosition);
                //display it or do something with it
             //   Toast.makeText(getContext(), " Clicked on :: " + headerInfo.getName()
             //           + "/" + detailInfo.getPrice(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                GroupInfo headerInfo = deptList.get(groupPosition);
                //display it or do something with it
          //      Toast.makeText(getContext(), " Header is :: " + headerInfo.getName(),
           //             Toast.LENGTH_LONG).show();

                return false;
            }
        });
        // makeJsonObjReq();

        return view;
    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            simpleExpandableListView.collapseGroup(i);
        }
    }


    //here we maintain our products in various departments
    private int addProduct(String department,String date, String product, String price, String quantity) {

        int groupPosition = 0;

        //check the hash map if the group already exists
        GroupInfo headerInfo = subjects.get(department);
        //add the group if doesn't exists
        if (headerInfo == null) {
            headerInfo = new GroupInfo();
            headerInfo.setName(department);
            headerInfo.setDate(date);
            subjects.put(department, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<ChildInfo> productList = headerInfo.getProductList();
        //size of the children list

        //create a new child and add that to the group
        ChildInfo detailInfo = new ChildInfo();
        detailInfo.setPrice(price);
        detailInfo.setProduct_id(product);
        detailInfo.setQuantity(quantity);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    private void makeJsonObjReq() {
        if (!cd.isConnectingToInternet()) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.NoConnection, Snackbar.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("user_id", sd.getUserId());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Constant.ORDER_HISTORY, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("DADADADADADAD", response.toString());
                            try {
                                progressDialog.dismiss();
                                subjects.clear();
                                deptList.clear();
                                String code = response.getString("code");
                                if (code.equalsIgnoreCase("200")) {
                                    JSONArray jsonArray = response.getJSONArray("response");
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String order_id = jsonObject.optString("order_id", "false");

                                        JSONArray array = jsonObject.getJSONArray("product");
                                        for (int j = 0; j < array.length(); j++) {

                                            JSONObject object = array.getJSONObject(j);
                                            String price = object.getString("price");
                                            String product = object.getString("product");
                                            String quantity = object.getString("quantity");
                                            String date = "2018-20-21";

                                            addProduct(order_id,date, product, price, quantity);
                                            //   list.add(model);
                                        }

                                    }
                                    listAdapter = new CustomAdapter(getContext(), deptList);
                                    // attach the adapter to the expandable list view
                                    simpleExpandableListView.setAdapter(listAdapter);
                                    expandAll();
                                }else {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "No Order History", Snackbar.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("DADADADADADAD", "Error: " + error.getMessage());
                    progressDialog.dismiss();

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
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjReq);
//        jsonObjReq.setTag(TAG);
            // Adding request to request queue
            queue.add(jsonObjReq);

            // Cancelling request
    /* if (queue!= null) {
    queue.cancelAll(TAG);
    } */

        }
    }
}